package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.dto.manager.*;
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

    @Autowired
    ServiceInvokeManager serviceInvokeManager;

    @Autowired
    ServiceToMachineProtocol serviceToMachineProtocol;

    @Autowired
    IDeviceService deviceService;

    /**
     * 机器状态改变事件
     * 【FM状态改变协议】
     * @param deviceDTO
     */
    public void changeStatusEvent(DeviceDTO deviceDTO) {

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
    }

    /**
     * 收到费率表事件
     * 【FM状态改变协议】
     * @param deviceDTO
     */
    public void rateTableUpdateEvent(DeviceDTO deviceDTO) {


        //访问俄罗斯服务器，改变状态
        ApiResponse changeStatusResponse = serviceInvokeManager.frankMachines(deviceDTO);
        //todo 收到了俄罗斯消息

        if (changeStatusResponse.isOK()) {
            // 更新数据库

        }
    }


    /**
     * 【机器请求授权协议】调用本方法
     *
     * @param deviceDTO
     */
    public void auth(DeviceDTO deviceDTO) throws Exception {
        //todo 收到了FM消息

        //访问俄罗斯服务器，请求授权
        ApiResponse authResponse = serviceInvokeManager.auth(deviceDTO.getId(), deviceDTO);

        //todo 收到了俄罗斯消息

        //todo 更新数据库中机器状态


        //服务器向机器返回授权结果 这里需要吗?
//        serviceToMachineProtocol.authResult(authResponse);

        //如果授权成功，更新公钥，发送给俄罗斯
        if (authResponse.isOK()) {
            //todo 更新公钥
            PublicKeyDTO publicKeyDTO = new PublicKeyDTO();
            publicKeyDTO.setKey("");
            publicKeyDTO.setRevision(0);
            publicKeyDTO.setExpireDate("");

            ApiResponse publickeyResponse = serviceInvokeManager.publicKey(deviceDTO.getId(), publicKeyDTO);

            if (publickeyResponse.isOK()) {
                //todo 更新公钥数据库
            }
        }

    }


    /**
     * 【机器请求取消授权协议】调用本方法
     *
     * @param deviceDTO
     */
    public void unauth(DeviceDTO deviceDTO) {
        //todo 收到了FM消息

        ApiResponse unauthResponse = serviceInvokeManager.unauth(deviceDTO.getId(), deviceDTO);
        //todo 收到了俄罗斯消息

        //更新数据库


    }

    /**
     * 【机器请求lost协议】调用本方法
     *
     * @param deviceDTO
     */
    public void lost(DeviceDTO deviceDTO) {
        //todo 收到了FM消息

        ApiResponse unauthResponse = serviceInvokeManager.lost(deviceDTO.getId(), deviceDTO);
        //todo 收到了俄罗斯消息

        //更新数据库

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
