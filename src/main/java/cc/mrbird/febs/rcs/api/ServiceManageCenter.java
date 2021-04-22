package cc.mrbird.febs.rcs.api;

import cc.mrbird.febs.rcs.dto.manager.*;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import lombok.NoArgsConstructor;
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
public class ServiceManageCenter {

    @Autowired
    ServiceInvokeManager serviceInvokeManager;

    @Autowired
    ServiceToMachineProtocol serviceToMachineProtocol;


    /**
     * 机器状态改变事件
     * 【FM状态改变协议】
     * @param frankMachineDTO
     */
    public void changeStatusEvent(FrankMachineDTO frankMachineDTO) {


        //访问俄罗斯服务器，改变状态
        ApiResponse changeStatusResponse = serviceInvokeManager.frankMachines(frankMachineDTO);
        //todo 收到了俄罗斯消息

        if (changeStatusResponse.isOK()) {
            FrankMachineDTO responseFrankMachineDTO = (FrankMachineDTO) changeStatusResponse.getObject();
            // 更新数据库

        }
    }

    /**
     * 收到费率表事件
     * 【FM状态改变协议】
     * @param frankMachineDTO
     */
    public void rateTableUpdateEvent(FrankMachineDTO frankMachineDTO) {


        //访问俄罗斯服务器，改变状态
        ApiResponse changeStatusResponse = serviceInvokeManager.frankMachines(frankMachineDTO);
        //todo 收到了俄罗斯消息

        if (changeStatusResponse.isOK()) {
            // 更新数据库

        }
    }


    /**
     * 【机器请求授权协议】调用本方法
     *
     * @param frankMachineDTO
     */
    public void auth(FrankMachineDTO frankMachineDTO) throws Exception {
        //todo 收到了FM消息

        //访问俄罗斯服务器，请求授权
        ApiResponse authResponse = serviceInvokeManager.auth(frankMachineDTO.getId(), frankMachineDTO);

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

            ApiResponse publickeyResponse = serviceInvokeManager.publicKey(frankMachineDTO.getId(), publicKeyDTO);

            if (publickeyResponse.isOK()) {
                //todo 更新公钥数据库
            }
        }

    }


    /**
     * 【机器请求取消授权协议】调用本方法
     *
     * @param frankMachineDTO
     */
    public void unauth(FrankMachineDTO frankMachineDTO) {
        //todo 收到了FM消息

        ApiResponse unauthResponse = serviceInvokeManager.unauth(frankMachineDTO.getId(), frankMachineDTO);
        //todo 收到了俄罗斯消息

        //更新数据库


    }

    /**
     * 【机器请求lost协议】调用本方法
     *
     * @param frankMachineDTO
     */
    public void lost(FrankMachineDTO frankMachineDTO) {
        //todo 收到了FM消息

        ApiResponse unauthResponse = serviceInvokeManager.lost(frankMachineDTO.getId(), frankMachineDTO);
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
