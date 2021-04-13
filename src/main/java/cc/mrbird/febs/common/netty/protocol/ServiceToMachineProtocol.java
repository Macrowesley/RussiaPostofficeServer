package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.asu.entity.manager.ApiResponse;
import cc.mrbird.febs.asu.entity.manager.ManagerBalance;
import cc.mrbird.febs.asu.entity.service.ChangeStatusRequest;
import cc.mrbird.febs.asu.entity.service.TaxVersion;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperUtils;
import cc.mrbird.febs.common.netty.protocol.kit.TempKeyUtils;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ServiceToMachineProtocol extends BaseProtocol {
    @Autowired
    public TempKeyUtils tempKeyUtils;

    public ServiceToMachineProtocol() {
    }

    /**
     * 服务器发送打开机器ssh指令
     * @param acnum
     */
    public void openSshProtocol(String acnum) {
        try {
            //执行指令
            /*try {
                String cmd = "cmd /c net start cygsshd";
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }*/


            ChannelHandlerContext ctx = ChannelMapperUtils.getChannelByKey(acnum);
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /**
             *     typedef  struct{
             *         unsigned char length;				 //一个字节
             *         unsigned char head;				 	 //0xC1
             *         unsigned char content[?];             //加密后内容 版本内容(3) + 域名(17) + 域名端口(4) + ssh端口(2) + 密码(11)
             *         unsigned char check;				     //校验位
             *         unsigned char tail;					 //0xD0
             *     }__attribute__((packed))ssh, *ssh;
             */
            //准备数据
            String version = "001";
            String domain = "device.uprins.com";
            String domainPort = "9091";
            String sshPort = "22";
            String sshPwd = "GDPT2020lai";
            String content = version + domain + domainPort + sshPort + sshPwd;
            String entryctContent = AESUtils.encrypt(content, tempKey);

            //发送数据
            wrieteToCustomer(ctx, getWriteContent(BaseTypeUtils.stringToByte(entryctContent, BaseTypeUtils.UTF8), (byte) 0xC1));
            log.info("服务器发送打开机器ssh指令");
        } catch (Exception e) {
            log.error("服务器发送打开机器ssh指令失败，原因如下：" + e.getMessage());
        }
    }

    /**
     * 服务器发送关闭机器ssh指令
     * @param acnum
     */
    public void closeSshProtocol(String acnum) {
        try {

            ChannelHandlerContext ctx = ChannelMapperUtils.getChannelByKey(acnum);
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /**
             *     typedef  struct{
             *         unsigned char length;				 //一个字节
             *         unsigned char head;				 	 //0xC2
             *         unsigned char content[?];             //加密后内容 版本内容(3)
             *         unsigned char check;				     //校验位
             *         unsigned char tail;					 //0xD0
             *     }__attribute__((packed))ssh, *ssh;
             */
            //准备数据
            String version = "001";
            String entryctContent = AESUtils.encrypt(version, tempKey);

            //发送数据
            wrieteToCustomer(ctx, getWriteContent(BaseTypeUtils.stringToByte(entryctContent, BaseTypeUtils.UTF8), (byte) 0xC2));
            log.info("服务器发送关闭机器ssh指令");


        } catch (Exception e) {
            log.error("服务器发送关闭机器ssh指令失败，原因如下：" + e.getMessage());
        } finally {
            //执行关闭指令
            /*try {
                String cmd = "cmd /c net stop cygsshd &&" +
                        "taskkill /IM sshd.exe /F &&" +
                        "taskkill /IM bash.exe /F";
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    /**
     * 服务器向机器返回授权结果 todo 待定
     * @param response
     */
    public void authResult(ApiResponse response) {

    }

    /**
     * 服务器改变机器状态
     * @param frankMachineId
     * @param changeStatusRequest
     */
    public void changeStatus(String frankMachineId, ChangeStatusRequest changeStatusRequest) {
        //TODO 根据frankMachineId得到Acnum  考虑把协议中的Acnum改成frankMachineId

        /**
         状态数字的含义
         1 ENABLED
         2 DEMO
         3 BLOCKED
         4 UNAUTHORIZED
         5 LOST
         typedef  struct{
             unsigned char length;				 //一个字节
             unsigned char head;				 	 //0xC3
             unsigned char content[?];            //加密后内容 版本内容(3) + frankMachineId() + 状态（1 ）+ postOffice()
             unsigned char check;				 //校验位
             unsigned char tail;					 //0xD0
         }__attribute__((packed))status, *status;
         **/



    }

    /**
     * 同步tax信息
     * todo 所有FM都需要同步tax信息吗？
     * @param taxVersion
     */
    public void updateTaxes(TaxVersion taxVersion) {
        /**
         *
         同步tax信息(待定，可能是文件)
         typedef  struct{
             unsigned char length;				 //一个字节
             unsigned char head;				 	 //0xC4
             unsigned char content[?];            //加密后内容 版本内容(3) + taxId(自己数据库中的 )  + ？？
             unsigned char check;				 //校验位
             unsigned char tail;					 //0xD0
         }__attribute__((packed))updateTaxes, *updateTaxes;
         */

    }

    /**
     * 同步余额
     * @param frankMachineId
     * @param managerBalance
     */
    public void balance(String frankMachineId, ManagerBalance managerBalance) {
        /**
         typedef  struct{
             unsigned char length;				 //一个字节
             unsigned char head;				 	 //0xC5
             unsigned char content[?];            //加密后内容 版本内容(3) + frankMachineId() + balanceId(?) + contractId(?) + contractNum(?) + current(?) + consolidate(?)
             unsigned char check;				 //校验位
             unsigned char tail;					 //0xD0
         }__attribute__((packed))updateTaxes, *updateTaxes;
         */


    }
}
