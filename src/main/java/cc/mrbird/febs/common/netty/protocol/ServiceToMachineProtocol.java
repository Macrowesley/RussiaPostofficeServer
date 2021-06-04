package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.StatusFMDTO;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperUtils;
import cc.mrbird.febs.common.netty.protocol.kit.TempKeyUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.dto.manager.DeviceDTO;
import cc.mrbird.febs.rcs.dto.manager.ManagerBalanceDTO;
import cc.mrbird.febs.rcs.dto.service.ChangeStatusRequestDTO;
import cc.mrbird.febs.rcs.dto.service.TaxVersionDTO;
import cc.mrbird.febs.rcs.entity.PublicKey;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceToMachineProtocol extends BaseProtocol {
    @Autowired
    public TempKeyUtils tempKeyUtils;

    @Autowired
    IDeviceService deviceService;

    public ServiceToMachineProtocol() {
    }

    /**
     * 服务器发送打开机器ssh指令
     * @param acnum
     */
    public boolean openSshProtocol(String acnum) {
        try {
            //执行指令
            /*try {
                String cmd = "cmd /c net start cygsshd";
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }*/


            ChannelHandlerContext ctx = ChannelMapperUtils.getChannelByAcnum(acnum);
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /**
             *     typedef  struct{
             *         unsigned char length[2];				 //2个字节
             *         unsigned char head;				 	 //0xC1
             *         unsigned char content[?];             //加密后内容 版本内容(3) + 域名(17) + 域名端口(4) + ssh端口(2) + 密码(11)
             *         unsigned char check;				     //校验位
             *         unsigned char tail;					 //0xD0
             *     }__attribute__((packed))ssh, *ssh;
             */
            //准备数据
            String version = FebsConstant.FmVersion1;
            String domain = "device.uprins.com";
            String domainPort = "9091";
            String sshPort = "22";
            String sshPwd = "GDPT2020lai";
            String content = version + domain + domainPort + sshPort + sshPwd;
            String entryctContent = AESUtils.encrypt(content, tempKey);

            //发送数据
            wrieteToCustomer(ctx, getWriteContent(BaseTypeUtils.stringToByte(entryctContent, BaseTypeUtils.UTF8), (byte) 0xC1));
            log.info("服务器发送打开机器ssh指令");
            return true;
        } catch (Exception e) {
            log.error("服务器发送打开机器ssh指令失败，原因如下：" + e.getMessage());
            return false;
        }
    }

    /**
     * 服务器发送关闭机器ssh指令
     * @param acnum
     */
    public boolean closeSshProtocol(String acnum) {
        try {

            ChannelHandlerContext ctx = ChannelMapperUtils.getChannelByAcnum(acnum);
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /**
             *     typedef  struct{
             *         unsigned char length[2];				 //2个字节
             *         unsigned char head;				 	 //0xC2
             *         unsigned char content[?];             //加密后内容 版本内容(3)
             *         unsigned char check;				     //校验位
             *         unsigned char tail;					 //0xD0
             *     }__attribute__((packed))ssh, *ssh;
             */
            //准备数据
            String version = FebsConstant.FmVersion1;
            String entryctContent = AESUtils.encrypt(version, tempKey);

            //发送数据
            wrieteToCustomer(ctx, getWriteContent(BaseTypeUtils.stringToByte(entryctContent, BaseTypeUtils.UTF8), (byte) 0xC2));
            log.info("服务器发送关闭机器ssh指令");

            return true;
        } catch (Exception e) {
            log.error("服务器发送关闭机器ssh指令失败，原因如下：" + e.getMessage());
            return false;
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
     * 服务器改变机器状态
     * @param frankMachineId
     * @param changeStatusRequestDTO
     */
    @Async(FebsConstant.ASYNC_POOL)
    public void changeStatus(String frankMachineId, ChangeStatusRequestDTO changeStatusRequestDTO) {

        try {
            ChannelHandlerContext ctx = ChannelMapperUtils.getChannelByAcnum(getAcnumByFMId(frankMachineId));
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);

            /**
             typedef  struct{
                 unsigned char length[2];				 //2个字节
                 unsigned char head;				 //0xC3
                 unsigned char version[3];			 //版本内容(3)
                 unsigned char content[?];           //加密后内容   StatusDTO的json 包含了：frankMachineId  + status+ postOffice
                 unsigned char check;				 //校验位
                 unsigned char tail;				 //0xD0
             }__attribute__((packed))status, *status;
             **/
            //准备数据
            String version = FebsConstant.FmVersion1;
            StatusFMDTO statusFMDTO = new StatusFMDTO();
            statusFMDTO.setFrankMachineId(frankMachineId);
            statusFMDTO.setPostOffice(changeStatusRequestDTO.getPostOffice());
            statusFMDTO.setStatus(changeStatusRequestDTO.getStatus().getCode());
            statusFMDTO.setEvent(1);

            /*int status = changeStatusRequestDTO.getStatus().getType();
            String postOffice = changeStatusRequestDTO.getPostOffice();*/

            String content = JSON.toJSONString(statusFMDTO);
            String entryctContent = AESUtils.encrypt(content, tempKey);
            log.info("服务器改变机器状态 content={},加密后entryctContent={}", content, entryctContent);
            //发送数据
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                    (byte) 0xC3));
        }catch (Exception e){
            log.error("服务器改变机器状态" + e.getMessage());
            //待处理
            throw new FmException(e.getMessage());
        }
    }

    /**
     * 发送privateKey给机器
     */
    @Async(FebsConstant.ASYNC_POOL)
    public void sentPrivateKey(String frankMachineId, PublicKey dbPublicKey){
        try {
            if (dbPublicKey.getFlow() == FlowEnum.FlowIng.getCode()){
                log.info("privateKey正在处理中，请等待");
                return;
            }

            ChannelHandlerContext ctx = ChannelMapperUtils.getChannelByAcnum(getAcnumByFMId(frankMachineId));
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //准备数据
            String version = FebsConstant.FmVersion1;

            /**
             typedef  struct{
                 unsigned char length[2];			 //2个字节
                 unsigned char head;				 	 //0xC6
                 unsigned char version[3];			 //版本内容(3)
                 unsigned char content[?];            //加密后内容  privateKey 的加密内容
                 unsigned char check;				 //校验位
                 unsigned char tail;					 //0xD0
             }__attribute__((packed))privateKey, *privateKey;
             */
            String content = dbPublicKey.getPrivateKey();
            String entryctContent = AESUtils.encrypt(content, tempKey);
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                            (byte) 0xC6));
            log.info("服务器发送privateKey给机器 content={},加密后entryctContent={}", content, entryctContent);
        } catch (Exception e) {
            throw new FmException(e.getMessage());
        }
    }

     /*
     ***********************************************************
     不确定方法
     ************************************************************
     */
    /**
     * 同步tax信息
     * todo 所有FM都需要同步tax信息吗？
     * @param taxVersionDTO
     */
    @Async(FebsConstant.ASYNC_POOL)
    public void updateTaxes(TaxVersionDTO taxVersionDTO) {
        /**
         *
         同步tax信息(待定，可能是文件)
         typedef  struct{
             unsigned char length[2];				 //2个字节
             unsigned char head;				 	 //0xC5
             unsigned char content[?];            //加密后内容 版本内容(3) + taxId(自己数据库中的 )  + ？？
             unsigned char check;				 //校验位
             unsigned char tail;					 //0xD0
         }__attribute__((packed))updateTaxes, *updateTaxes;
         */

    }

    /**
     * 同步余额
     * @param frankMachineId
     * @param managerBalanceDTO
     */
    public void balance(String frankMachineId, ManagerBalanceDTO managerBalanceDTO) {
        /**
         typedef  struct{
             unsigned char length[2];				 //2个字节
             unsigned char head;				 	 //0xC6
             unsigned char content[?];            //加密后内容 版本内容(3) + frankMachineId() + balanceId(?) + contractId(?) + contractNum(?) + current(?) + consolidate(?)
             unsigned char check;				 //校验位
             unsigned char tail;					 //0xD0
         }__attribute__((packed))updateTaxes, *updateTaxes;
         */

        //todo 需要同步账户余额

    }

    /*
     ***********************************************************
     私有方法
     ************************************************************
     */
    private String getAcnumByFMId(String frankMachineId) {
        return deviceService.getAcnumByFMId(frankMachineId);
    }

}
