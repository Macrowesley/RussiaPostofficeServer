package cc.mrbird.febs.asu;

import cc.mrbird.febs.asu.entity.enums.Event;
import cc.mrbird.febs.asu.entity.manager.*;
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
     * 【FM状态改变协议】调用本方法
     *
     * @param frankMachine
     */
    public void changeStatus(FrankMachine frankMachine) {
        //todo 收到了FM消息
        switch (frankMachine.getEvent()) {
            case STATUS:
                changeStatusEvent(frankMachine);
                break;
            case RATE_TABLE_UPDATE:
                rateTableUpdateEvent(frankMachine);
                break;
            default:
                //todo 处理异常
                break;
        }
    }

    /**
     * 机器状态改变事件
     *
     * @param frankMachine
     */
    private void changeStatusEvent(FrankMachine frankMachine) {


        //访问俄罗斯服务器，改变状态
        ApiResponse changeStatusResponse = serviceInvokeManager.frankMachines(frankMachine);
        //todo 收到了俄罗斯消息

        if (changeStatusResponse.isOK()) {

            // 更新数据库

        }
    }

    /**
     * 收到费率表事件
     *
     * @param frankMachine
     */
    private void rateTableUpdateEvent(FrankMachine frankMachine) {


        //访问俄罗斯服务器，改变状态
        ApiResponse changeStatusResponse = serviceInvokeManager.frankMachines(frankMachine);
        //todo 收到了俄罗斯消息

        if (changeStatusResponse.isOK()) {
            // 更新数据库

        }
    }


    /**
     * 【机器请求授权协议】调用本方法
     *
     * @param frankMachine
     */
    public void auth(FrankMachine frankMachine) throws Exception {
        //todo 收到了FM消息

        //访问俄罗斯服务器，请求授权
        ApiResponse authResponse = serviceInvokeManager.auth(frankMachine.getId(), frankMachine);

        //todo 收到了俄罗斯消息

        //todo 更新数据库中机器状态


        //服务器向机器返回授权结果 这里需要吗?
//        serviceToMachineProtocol.authResult(authResponse);

        //如果授权成功，更新公钥，发送给俄罗斯
        if (authResponse.isOK()) {
            //todo 更新公钥
            PublicKey publicKey = new PublicKey();
            publicKey.setKey("");
            publicKey.setRevision(0);
            publicKey.setExpireDate("");

            ApiResponse publickeyResponse = serviceInvokeManager.publicKey(frankMachine.getId(), publicKey);

            if (publickeyResponse.isOK()) {
                //todo 更新公钥数据库
            }
        }

    }


    /**
     * 【机器请求取消授权协议】调用本方法
     *
     * @param frankMachine
     */
    public void unauth(FrankMachine frankMachine) {
        //todo 收到了FM消息

        ApiResponse unauthResponse = serviceInvokeManager.unauth(frankMachine.getId(), frankMachine);
        //todo 收到了俄罗斯消息

        //更新数据库


    }

    /**
     * 【机器请求lost协议】调用本方法
     *
     * @param frankMachine
     */
    public void lost(FrankMachine frankMachine) {
        //todo 收到了FM消息

        ApiResponse unauthResponse = serviceInvokeManager.lost(frankMachine.getId(), frankMachine);
        //todo 收到了俄罗斯消息

        //更新数据库

    }

    /**
     * 【机器请求foreseens协议】调用本方法
     * 请求打印任务
     */
    public void foreseens(Foreseen foreseen) {
        //todo 收到了FM消息

        //todo 从数据库中给foreseen补充信息

        ApiResponse foreseensResponse = serviceInvokeManager.foreseens(foreseen);
        //todo 收到了俄罗斯消息

        if (foreseensResponse.isOK()){
            //todo 更新数据库
            ManagerBalance managerBalance = (ManagerBalance) foreseensResponse.getObject();


            //todo 发送消息给机器
            serviceToMachineProtocol.balance(foreseen.getFrankMachineId(), managerBalance);
        }
    }

    /**
     * 【机器请求transactions协议】调用本方法
     * 交易
     */
    public void transactions(Transaction transaction) {
        //todo 收到了FM消息

        //todo 从数据库中给transaction补充信息



        ApiResponse transactionsResponse = serviceInvokeManager.transactions(transaction);
        //todo 收到了俄罗斯消息

        if (transactionsResponse.isOK()){
            //todo 更新数据库
            ManagerBalance managerBalance = (ManagerBalance) transactionsResponse.getObject();


            //todo 发送消息给机器
            serviceToMachineProtocol.balance(transaction.getFrankMachineId(), managerBalance);
        }
    }

    /**
     * 【机器请求cancelJob协议】调用本方法
     * 取消任务
     */
    public void cancelJob(String frankMachineId, String foreseenId, String cancelMessage) {
        //todo 收到了FM消息

        //todo 从数据库中给transaction补充信息



        ApiResponse cancelResponse = serviceInvokeManager.cancel(foreseenId, new ForeseenCancel("cancelMessage"));
        //todo 收到了俄罗斯消息

        if (cancelResponse.isOK()){
            //todo 更新数据库
            ManagerBalance managerBalance = (ManagerBalance) cancelResponse.getObject();


            //todo 发送消息给机器
            serviceToMachineProtocol.balance(frankMachineId, managerBalance);
        }
    }
}
