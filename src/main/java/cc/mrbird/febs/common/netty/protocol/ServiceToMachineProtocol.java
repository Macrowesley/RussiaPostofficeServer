package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.StatusFMDTO;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
import cc.mrbird.febs.common.netty.protocol.kit.TempKeyUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MD5Util;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.manager.ManagerBalanceDTO;
import cc.mrbird.febs.rcs.dto.service.ChangeStatusRequestDTO;
import cc.mrbird.febs.rcs.dto.service.TaxVersionDTO;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.entity.TaxDeviceUnreceived;
import cc.mrbird.febs.rcs.service.ITaxDeviceUnreceivedService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ServiceToMachineProtocol extends BaseProtocol {
    @Autowired
    public TempKeyUtils tempKeyUtils;

    @Autowired
    IDeviceService deviceService;

    @Autowired
    ITaxDeviceUnreceivedService taxDeviceUnreceivedService;

    @Autowired
    ChannelMapperManager channelMapperManager;

    public ServiceToMachineProtocol() {
    }

    @Override
    public BaseProtocol getOperator() {
        return null;
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


            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(acnum);
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /**
             *     typedef  struct{
             *         unsigned char length[4];				 //4个字节
             *         unsigned char type;				 	 //0xC1
             *         unsigned char  operateID[2];
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

            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(acnum);
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /**
             *     typedef  struct{
             *         unsigned char length[4];				 //4个字节
             *         unsigned char type;				 	 //0xC2
             *         unsigned char  operateID[2];
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
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);

            /**
             typedef  struct{
                 unsigned char length[4];				 //4个字节
                 unsigned char type;				 //0xC3
                 unsigned char  operateID[2];
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
            statusFMDTO.setIsAuto(2);

            /*int status = changeStatusRequestDTO.getStatus().getType();
            String postOffice = changeStatusRequestDTO.getPostOffice();*/

            String content = JSON.toJSONString(statusFMDTO);
            String entryctContent = AESUtils.encrypt(content, tempKey);
            log.info("服务器主动改变机器状态 content={},加密后entryctContent={}", content, entryctContent);
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
    public void noticeMachineUpdateKey(String frankMachineId, PublicKey dbPublicKey){
        try {
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //准备数据
            String version = FebsConstant.FmVersion1;

            /**
             typedef  struct{
                 unsigned char length[4];			 //4个字节
                 unsigned char type;				 	 //0xC6
                 unsigned char  operateID[2];
                 unsigned char version[3];			 //版本内容(3)
                 unsigned char content[?];            //加密后内容   Key revision(4位，不够用0填充)
                 unsigned char check;				 //校验位
                 unsigned char tail;					 //0xD0
             }__attribute__((packed))privateKey, *privateKey;
             */
            String content = dbPublicKey.getSuccessMsg();
            String entryctContent = AESUtils.encrypt(content, tempKey);
            log.info("服务器通知机器更新key content={},加密后entryctContent={}", content, entryctContent);
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                            (byte) 0xC6));
        } catch (Exception e) {
            throw new FmException(e.getMessage());
        }
    }

    /**
     * 发送tax信息给所有机器
     */
    public void sendTaxToAllMachine(TaxVersionDTO taxVersionDTO, String jsonFileName){

        log.info("记录机器和版本的信息");
        List<Device> deviceList = deviceService.list();
        List<TaxDeviceUnreceived> taxDeviceUnreceivedList = new ArrayList<>();
        deviceList.stream().forEach(device -> {
            TaxDeviceUnreceived temp = new TaxDeviceUnreceived();
            temp.setFrankMachineId(device.getFrankMachineId());
            temp.setTaxVersion(taxVersionDTO.getVersion());
            temp.setCreatedTime(new Date());
            taxDeviceUnreceivedList.add(temp);
        });
        //所有机器都应该收到
        taxDeviceUnreceivedService.saveUniqueBatch(taxDeviceUnreceivedList);

        //但是只能给当前在线的机器发送信息
        log.info("开始发送tax给所有的机器");
        ConcurrentHashMap<String, ChannelHandlerContext> loginChannelMap = channelMapperManager.getLoginChannelMap();
        Iterator<Map.Entry<String, ChannelHandlerContext>> entries = loginChannelMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, ChannelHandlerContext> entry = entries.next();

            //发送
            sendTaxesToMachine(
                    entry.getKey(),
                    entry.getValue(),
                    taxVersionDTO.getVersion(),
                    DateKit.parseRussiatime(taxVersionDTO.getApplyDate()),
                    JSON.toJSONString(taxVersionDTO),
                    jsonFileName,
                    false
            );
        }
    }

    /**
     * 发送所有tax信息给机器
     * @param taxVersionDTO
     */
    @Async(FebsConstant.NETTY_ASYNC_POOL)
    public void sendTaxesToMachine(String frankMachineId, ChannelHandlerContext ctx, String taxVersion, Date applyDate, String taxJson, String jsonFileName, boolean isNeedSaveToDb) {
        String applyDateStr = DateKit.formatDateYmdhms(applyDate);
        log.info("【协议开始 给机器" + frankMachineId + "发送tax信息】, applyDate = "  + applyDateStr);
        /**
         *
         同步tax信息
         typedef  struct{
         unsigned char length[4];				 //4个字节
         unsigned char type;				 	 //0xC4
         unsigned char  operateID[2];
         unsigned char version[3];			  //版本内容(3)
         unsigned char content[?];            //不加密内容 yyyymmddhhmmss(14) + md5(32) + url(56)http://russia.uprins.com:90/tax/2021_07_06_16_13_47.json
         unsigned char check;				 //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))sendTaxes, *sendTaxes;
         */
        try {
            if (ctx == null) {
                ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            }
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //准备数据
            String version = FebsConstant.FmVersion1;

            String md5Str = MD5Util.MD5Encode(taxJson).toLowerCase();
            String url = "http://russia.uprins.com:90/tax/" + jsonFileName + ".json";
            String content = applyDateStr + md5Str + url;
//            String entryctContent = AESUtils.encrypt(content, tempKey);
//            log.info("服务器发送tax给机器 content={},加密后entryctContent={}", content, entryctContent);
            log.info("服务器发送tax给机器 applyDateStr={} md5 = {}, tax长度={}",applyDateStr, md5Str, taxJson.length() );
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + content, BaseTypeUtils.UTF8),
                            (byte) 0xC4));

            //需要记录到延迟队列中或者记录到数据库中
            if (isNeedSaveToDb){
                TaxDeviceUnreceived temp = new TaxDeviceUnreceived();
                temp.setFrankMachineId(frankMachineId);
                temp.setTaxVersion(taxVersion);
                temp.setCreatedTime(new Date());
                taxDeviceUnreceivedService.saveOneUnique(temp);
            }
            log.info("【协议结束 发送tax给机器】");
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
     * 同步余额
     * @param frankMachineId
     * @param managerBalanceDTO
     */
    public void balance(String frankMachineId, ManagerBalanceDTO managerBalanceDTO) {
        /**
         typedef  struct{
             unsigned char length[4];				 //4个字节
             unsigned char type;				 	 //0xC8
             unsigned char  operateID[2];
             unsigned char content[?];            //加密后内容 版本内容(3) + frankMachineId() + balanceId(?) + contractCode(?) + contractNum(?) + current(?) + consolidate(?)
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
    private String getAcnumByFmId(String frankMachineId) {
        String acnum;
        try {
            acnum = (String)redisService.get(frankMachineId);
            if (!StringUtils.isEmpty(acnum)){
                return acnum;
            }
            acnum = deviceService.getAcnumByFMId(frankMachineId);
            redisService.set(frankMachineId,acnum);
            return acnum;
        }catch (Exception e){
            return deviceService.getAcnumByFMId(frankMachineId);
        }
    }

}
