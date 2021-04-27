package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FMStatusEnum;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.enums.ResultEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.common.kit.PublicKeyKit;
import cc.mrbird.febs.rcs.dto.manager.*;
import cc.mrbird.febs.rcs.service.IPublicKeyService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 管理中心
 * 主要作用：
 * 1. 机器访问服务器的协议中调用本类的方法，一个协议调用一个方法，
 * 2. 处理访问服务器的返回
 * 3. 处理1,2之间的数据库更新
 */
@Component
@NoArgsConstructor
@Slf4j
public class ServiceManageCenter {
    int waitTime = 1;

    @Autowired
    ServiceInvokeManager serviceInvokeManager;

    @Autowired
    ServiceToMachineProtocol serviceToMachineProtocol;

    @Autowired
    IDeviceService deviceService;

    @Autowired
    IPublicKeyService publicKeyService;

    /**
     * 机器状态改变事件
     * 【FM状态改变协议】
     * @param deviceDTO
     */
    public boolean changeStatusEvent(DeviceDTO deviceDTO) {

        //判断再次访问这个接口的时候，需要的验证
        /**
         * 正常情况下，访问这个接口，机器的flow为 0
         * 当完成这个闭环的时候，flow为1了，当机器再次访问这个接口的时候
         * 如果flow为0 继续往后走
         * 如果flow为1，不继续了
         */

        Device dbDevice = deviceService.getDeviceByFrankMachineId(deviceDTO.getId());
        if (dbDevice.getFlow() != FlowEnum.FlowIng.getCode()){
            throw new FmException("机器的状态已经修改结束了，请勿操作");
        }

        //访问俄罗斯服务器，改变状态
        ApiResponse apiResponse = serviceInvokeManager.frankMachines(deviceDTO);

        //更新数据库
        deviceService.changeStatusEnd(deviceDTO, apiResponse.isOK());

        return apiResponse.isOK();
    }


    /**
     * 【机器请求授权协议】调用本方法
     *
     * @param deviceDTO
     */
    public boolean auth(DeviceDTO deviceDTO) throws Exception {
        String frankMachineId = deviceDTO.getId();
        log.info("服务器收到了设备{}发送的auth协议", frankMachineId);
        Device dbDevice = deviceService.getDeviceByFrankMachineId(frankMachineId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbDevice.getFlow());
        //当前的进度
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbDevice.getFlowDetail());
        FMStatusEnum dbFutureStatus = FMStatusEnum.getByCode(dbDevice.getFutureFmStatus());

        //是否是第一次请求授权
        boolean isFirstAuth = dbFlow == FlowEnum.FlowEnd;
        /**
         * 过滤：
         * 1. 闭环的不通过
         * 2. 未闭环，而且要改的状态是如果不是auth，也不通过
         */
        if (isFirstAuth && curFlowDetail == FlowDetailEnum.AuthEndSuccess){
            return true;
        }else{
            if(dbFutureStatus != FMStatusEnum.AUTHORIZED){
                //todo
                return false;
            }
        }


        /**
         * 接下来都是未闭环的操作
         * 情况1：第一次，全部走一遍
         *      is First Auth
         * 情况2：第一次碰到了问题，第二次再次走一遍
         *      is not First Auth
         *      - error1
         *          auth publickey
         *      - error2
         *          publickey
         *      - fail
         *          auth publickey
         */

        //访问俄罗斯服务器，请求授权

        //todo 什么条件才能调用这个方法
        if (isFirstAuth || curFlowDetail == FlowDetailEnum.AuthError1 || curFlowDetail == FlowDetailEnum.AuthEndFail) {
            ApiResponse authResponse = serviceInvokeManager.auth(frankMachineId, deviceDTO);

            if (!authResponse.isOK()) {
                if (authResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                    //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                    deviceService.changeAuthStatus(dbDevice,frankMachineId, FlowDetailEnum.AuthError1);
                    log.info("服务器收到了设备{}发送的auth协议，发送了消息给俄罗斯，未接收到俄罗斯返回", frankMachineId);
                } else {
                    //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器，不保存进度
                    deviceService.changeAuthStatus(dbDevice,frankMachineId, FlowDetailEnum.AuthEndFail);
                    log.info("服务器收到了设备{}发送的auth协议，发送了消息给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId);
                }
                return false;
            }
        }
        //如果授权成功，更新公钥，发送给俄罗斯
        //过期天数
        int expire = 7;
        PublicKeyDTO publicKeyDTO = new PublicKeyDTO();
        publicKeyDTO.setKey(PublicKeyKit.getPublicKey());
        publicKeyDTO.setRevision(0);
        publicKeyDTO.setExpireDate(DateKit.offsetDate(expire));

        //todo 什么条件才能调用这个方法
        if (isFirstAuth || curFlowDetail == FlowDetailEnum.AuthError2 || curFlowDetail == FlowDetailEnum.AuthEndFail) {
            ApiResponse publickeyResponse = serviceInvokeManager.publicKey(frankMachineId, publicKeyDTO);

            if (!publickeyResponse.isOK()) {
                if (publickeyResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                    //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                    deviceService.changeAuthStatus(dbDevice,frankMachineId, FlowDetailEnum.AuthError2);
                    log.info("服务器收到了设备{}发送的auth协议，发送了消息给俄罗斯，然后发送了publickey给俄罗斯，但是没有收到返回", frankMachineId);
                } else {
                    //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                    deviceService.changeAuthStatus(dbDevice,frankMachineId, FlowDetailEnum.AuthEndFail);
                    log.info("服务器收到了设备{}发送的auth协议，发送了消息给俄罗斯，然后发送了publickey给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId);
                }
                return false;
            }

            publicKeyService.saveOrUpdate(frankMachineId, publicKeyDTO);
            deviceService.changeAuthStatus(dbDevice,frankMachineId, FlowDetailEnum.AuthEndSuccess);
            log.info("服务器收到了设备{}发送的auth协议，发送了消息给俄罗斯，然后发送了publickey给俄罗斯，收到了俄罗斯返回", frankMachineId);
        }
        return true;
    }


    /**
     * 【机器请求取消授权协议】调用本方法
     *
     * @param deviceDTO
     */
    public boolean unauth(DeviceDTO deviceDTO) {
        String frankMachineId = deviceDTO.getId();
        Device dbDevice = deviceService.getDeviceByFrankMachineId(frankMachineId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbDevice.getFlow());
        //当前的进度
        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbDevice.getFlowDetail());
        FMStatusEnum dbFutureStatus = FMStatusEnum.getByCode(dbDevice.getFutureFmStatus());

        //是否是第一次请求授权
        boolean isFirstAuth = dbFlow == FlowEnum.FlowEnd;

        ApiResponse unauthResponse = serviceInvokeManager.unauth(deviceDTO.getId(), deviceDTO);
        //todo 收到了俄罗斯消息

        //更新数据库

        return true;
    }

    /**
     * 收到费率表事件
     * 【FM状态改变协议】
     * @param deviceDTO
     */
    public boolean rateTableUpdateEvent(DeviceDTO deviceDTO) {


        //访问俄罗斯服务器，改变状态
        ApiResponse changeStatusResponse = serviceInvokeManager.frankMachines(deviceDTO);
        //todo 收到了俄罗斯消息

        if (changeStatusResponse.isOK()) {
            // 更新数据库

        }
        return true;
    }

    /**
     * 【机器请求lost协议】调用本方法
     *
     * @param deviceDTO
     */
    public boolean lost(DeviceDTO deviceDTO) {
        //todo 收到了FM消息

        ApiResponse unauthResponse = serviceInvokeManager.lost(deviceDTO.getId(), deviceDTO);
        //todo 收到了俄罗斯消息

        //更新数据库

        return true;
    }

    /**
     * 【机器请求foreseens协议】调用本方法
     * 请求打印任务
     */
    public void foreseens(ForeseenDTO foreseenDTO) {
        //todo 先判断机器状态，然后判断 job是否完成

        //todo 收到了FM消息

        //todo 从数据库中给foreseen补充信息

        ApiResponse foreseensResponse = serviceInvokeManager.foreseens(foreseenDTO);
        //todo 收到了俄罗斯消息

        if (foreseensResponse.isOK()){
            //todo 更新数据库
            ManagerBalanceDTO managerBalanceDTO = (ManagerBalanceDTO) foreseensResponse.getObject();


            //todo 发送消息给机器
            serviceToMachineProtocol.balance(foreseenDTO.getFrankMachineId(), managerBalanceDTO);
        }
    }

    /**
     * 【机器请求transactions协议】调用本方法
     * 交易
     */
    public void transactions(TransactionDTO transactionDTO) {
        //todo 先判断机器状态，然后判断 job是否完成

        //todo 收到了FM消息

        //todo 从数据库中给transaction补充信息



        ApiResponse transactionsResponse = serviceInvokeManager.transactions(transactionDTO);
        //todo 收到了俄罗斯消息

        if (transactionsResponse.isOK()){
            //todo 更新数据库
            ManagerBalanceDTO managerBalanceDTO = (ManagerBalanceDTO) transactionsResponse.getObject();

            //todo 需要同步账户余额

            //todo 发送消息给机器
            serviceToMachineProtocol.balance(transactionDTO.getFrankMachineId(), managerBalanceDTO);
        }
    }

    /**
     * 【机器请求cancelJob协议】调用本方法
     * 取消任务
     */
    public void cancelJob(String frankMachineId, String foreseenId, String cancelMessage) {
        //todo 判断是否可以取消 查看状态
        //todo 收到了FM消息

        //todo 从数据库中给transaction补充信息



        ApiResponse cancelResponse = serviceInvokeManager.cancel(foreseenId, new ForeseenCancel("cancelMessage"));
        //todo 收到了俄罗斯消息

        if (cancelResponse.isOK()){
            //todo 更新数据库
            ManagerBalanceDTO managerBalanceDTO = (ManagerBalanceDTO) cancelResponse.getObject();


            //todo 发送消息给机器
            serviceToMachineProtocol.balance(frankMachineId, managerBalanceDTO);
        }
    }
}
