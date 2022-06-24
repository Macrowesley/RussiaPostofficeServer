package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.PcCancelInfoDTO;
import cc.mrbird.febs.common.netty.protocol.dto.StatusFMDTO;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
import cc.mrbird.febs.common.netty.protocol.kit.TempKeyUtils;
import cc.mrbird.febs.common.netty.protocol.machine.ForeseensPortocol;
import cc.mrbird.febs.common.utils.*;
import cc.mrbird.febs.device.dto.RemoteFileDTO;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.api.CheckUtils;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.common.enums.WebSocketEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.machine.AdImageInfo;
import cc.mrbird.febs.rcs.dto.machine.AdInfoDTO;
import cc.mrbird.febs.rcs.dto.machine.PrintProgressInfo;
import cc.mrbird.febs.rcs.dto.manager.ManagerBalanceDTO;
import cc.mrbird.febs.rcs.dto.service.ChangeStatusRequestDTO;
import cc.mrbird.febs.rcs.dto.service.TaxVersionDTO;
import cc.mrbird.febs.rcs.entity.*;
import cc.mrbird.febs.rcs.service.IMsgService;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import cc.mrbird.febs.rcs.service.ITaxDeviceUnreceivedService;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.mchange.lang.ByteUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    IPrintJobService printJobService;

    @Autowired
    ChannelMapperManager channelMapperManager;

    @Autowired
    IMsgService msgService;

    @Autowired
    CheckUtils checkUtils;

    @Value("${info.download-base-url}")
    private String downLoadBaseUrl;


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
             *         unsigned char content[?];             //加密后内容 版本内容(3)
             *         unsigned char check;				     //校验位
             *         unsigned char tail;					 //0xD0
             *     }__attribute__((packed))ssh, *ssh;
             */
            //准备数据
            String version = FebsConstant.FmVersion1;
   /*         String domain = "device.uprins.com";
            String domainPort = "9091";
            String sshPort = "22";
            String sshPwd = "GDPT2020lai";
            String content = version + domain + domainPort + sshPort + sshPwd;*/
            String entryctContent = AESUtils.encrypt(version, tempKey);

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
     * 更新远程文件
     * @param acnum
     * @return
     */
    public boolean updateRemoteFileProtocol(RemoteFileDTO remoteFileDTO) {
        try {

            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(remoteFileDTO.getAcnum());
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /*typedef  struct{
                unsigned char length[4];			 //4个字节
                unsigned char type;				 	 //0xC5
                unsigned char operateID[2];
                unsigned char content[?];            //加密后内容 版本内容(3) + RemoteFileDTO的json
                unsigned char check;				 //校验位
                unsigned char tail;					 //0xD0
            }__attribute__((packed))updateFile, *updateFile;

            public class RemoteFileDTO {
                Long deviceId;
                String acnum;
                String remoteFilePath;
                String url;
                String md5;
            }*/

            //准备数据
            String version = "001";
            //找到上传文件的绝对路径，得到文件内容
            String filePath = (String) redisService.get(remoteFileDTO.getUrl());
            byte[] bytes = FileUtil.readBytes(filePath);

            String md5Str = ByteUtils.toHexAscii(MD5Util.md5(bytes)).toLowerCase();
            remoteFileDTO.setMd5(md5Str);
            String entryctContent = AESUtils.encrypt(version + JSON.toJSONString(remoteFileDTO), tempKey);
            //发送数据
            wrieteToCustomer(ctx, getWriteContent(BaseTypeUtils.stringToByte(entryctContent, BaseTypeUtils.UTF8), (byte) 0xC5));
            log.info("服务器发送更新机器文件指令 " + remoteFileDTO.getRemoteFilePath());

//            redisService.del(remoteFileDTO.getUrl());
            return true;
        } catch (Exception e) {
            log.error("服务器发送更新机器文件指令，原因如下：" + e.getMessage());
            return false;
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
        log.info("taxDeviceUnreceivedList.size() = " + taxDeviceUnreceivedList.size());
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
            String url = downLoadBaseUrl + "tax/" + jsonFileName + ".json";
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



    /**
     * PC发送打印信息给机器,接入foreseen
     * 订单详情和打印进度
     */
    public void doPrintJob(PrintJob dbPrintJob) {
        try {
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(dbPrintJob.getFrankMachineId()));

            if (ctx == null) {
                throw new FebsException("机器" + dbPrintJob.getFrankMachineId() + "没有连接，无法操作");
            }


            FlowDetailEnum dbFlowDetail = FlowDetailEnum.getByCode(dbPrintJob.getFlowDetail());
            byte[] data = null;
            PrintProgressInfo productPrintProgress = printJobService.getProductPrintProgress(dbPrintJob);
            //判断foreseen是否结束
            if (dbFlowDetail == FlowDetailEnum.JobingPcCreatePrint || dbFlowDetail == FlowDetailEnum.JobingErrorForeseensUnKnow){
                //如果是第一次创建网络订单或者foreseen网络错误，服务器自己拼接Foreseen给俄罗斯，俄罗斯通过后，拼接地址，进度等信息给机器
                Device dbDevice = deviceService.checkAndGetDeviceByFrankMachineId(dbPrintJob.getFrankMachineId());

                ForeseenFMDTO foreseenFmDto = new ForeseenFMDTO();
                foreseenFmDto.setContractCode(dbPrintJob.getContractCode());
                foreseenFmDto.setFrankMachineId(dbPrintJob.getFrankMachineId());
                foreseenFmDto.setUserId(FebsUtil.getCurrentUser().getUsername());
                foreseenFmDto.setPostOffice(dbDevice.getPostOffice());
                foreseenFmDto.setTotalCount(dbPrintJob.getTotalCount());

                foreseenFmDto.setProducts(productPrintProgress.getProductArr());

                foreseenFmDto.setTaxVersion(dbDevice.getTaxVersion());
                foreseenFmDto.setTotalAmmount(String.valueOf(MoneyUtils.changeY2F(dbPrintJob.getTotalAmount())));
                foreseenFmDto.setMachineDate(DateUtil.getCurTime());
                foreseenFmDto.setPrintJobType(dbPrintJob.getType());
                foreseenFmDto.setPrintJobId(dbPrintJob.getId());

                //发送给客户端的数据
                data = serviceManageCenter.foreseens(foreseenFmDto, dbPrintJob, ctx);
            }else{
                //如果已经走通了foreseen,但是transaction没有成功，则发送消息，进度给机器，让机器继续打印
                Contract dbContract = checkUtils.checkContractIsOk(dbPrintJob.getContractCode());
                data = serviceManageCenter.buildForeseenResultBytes(dbPrintJob,ctx, dbPrintJob.getForeseenId(), dbContract, productPrintProgress);
            }

            wrieteToCustomer(ctx, getWriteContent(data, ForeseensPortocol.PROTOCOL_TYPE));

            msgService.sendMsg(WebSocketEnum.ClickPrintRes.getCode(), dbPrintJob.getId(),"");
            log.info("【协议结束 发送pc订单给机器】");
        } catch (Exception e) {
//            e.printStackTrace();
            throw new FmException(e.getMessage());
        }
    }



    /**
     * PC主动取消打印任务
     */
    public void cancelPrintJob(PrintJob dbPrintJob) {
        /*typedef  struct{
            unsigned char length[4];
            unsigned char type;				 	 //0xC8
            unsigned char operateID[2];
            unsigned char version[3];			 //版本内容(3)
            unsigned char content[?];            //加密后内容: pcCancelInfoDTO信息
            unsigned char check;				 //校验位
            unsigned char tail;					 //0xD0
        }__attribute__((packed))pcPrintJobCancel, *pcPrintJobCancel;

        */

        /**
         * 发送给机器，告知取消打印
         */
        try {
            log.info("【协议开始 发送pc 取消订单命令 给机器】");
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(dbPrintJob.getFrankMachineId()));
            if (ctx == null) {
                throw new FebsException("机器" + dbPrintJob.getFrankMachineId() + "没有连接，无法操作");
            }
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //准备数据
            String version = FebsConstant.FmVersion1;

            String content = JSON.toJSONString(
                    new PcCancelInfoDTO(
                            String.valueOf(FebsUtil.getCurrentUser().getUserId()),
                            String.valueOf(dbPrintJob.getId()),
                            dbPrintJob.getForeseenId() == null ? "" : dbPrintJob.getForeseenId()));
            String entryctContent = AESUtils.encrypt(content, tempKey);
            log.info("服务器 发送pc 取消订单命令 给机器 content={},加密后entryctContent={}", content, entryctContent);
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                            (byte) 0xC8));

            msgService.sendMsg(WebSocketEnum.CancelPrintRes.getCode(), dbPrintJob.getId(),"");
            log.info("【协议结束 发送pc 取消订单命令 给机器】");
        } catch (Exception e) {
            throw new FmException(e.getMessage());
        }
    }

    /**
     * 1. 发送批量广告图片给机器
     * 两种情况：
     * - 开机的时候，机器登录后，发给机器
     * - PC对图片有增删，也把最新的图片列表给机器
     * @param frankMachineId
     */
    public void syncImageList(String frankMachineId, AdImageInfo[] adImageInfoArr){
        /*typedef  struct{
            unsigned char length[4];
            unsigned char type;				 	 //0xCA
            unsigned char operateID[2];
            unsigned char version[3];			 //版本内容(3)
            unsigned char content[?];            //加密后内容: AdInfoDTO
            unsigned char check;				 //校验位
            unsigned char tail;					 //0xD0
        }__attribute__((packed))adInfo, *adInfo;*/

        try {
            log.info("【协议开始 发送批量广告图片 给机器】");
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            if (ctx == null) {
                throw new FebsException("机器" + frankMachineId + "没有连接，无法操作");
            }
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //准备数据
            String version = FebsConstant.FmVersion1;


            AdInfoDTO adInfoDTO = new AdInfoDTO();
            adInfoDTO.setAdImageList(adImageInfoArr);
            String content = JSON.toJSONString(adInfoDTO);
            String entryctContent = AESUtils.encrypt(content, tempKey);
            log.info("服务器 发送批量广告图片 给机器 content={},加密后entryctContent={}", content, entryctContent);
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                            (byte) 0xCA));

            log.info("【协议结束 发送批量广告图片 给机器】");
        } catch (Exception e) {
            throw new FmException(e.getMessage());
        }
    }

    /**
     * 清除机器累计金额
     * @param frankMachineId
     */
    public void clearFmMoney(String frankMachineId){
        /*typedef  struct{
            unsigned char length[4];			 //4个字节
            unsigned char type;				 	 //0xCC
            unsigned char operateID[2];
            unsigned char content[?];            //加密后内容 版本内容(3)
            unsigned char check;				 //校验位
            unsigned char tail;					 //0xD0
        }__attribute__((packed))clearMoney, *clearMoney;*/


        try {
            log.info("【协议开始 清除机器累计金额 给机器】");
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            if (ctx == null) {
                throw new FebsException("机器" + frankMachineId + "没有连接，无法操作");
            }
            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //准备数据
            String version = FebsConstant.FmVersion1;

            String entryctContent = AESUtils.encrypt(version, tempKey);

            log.info("服务器 清除机器累计金额 给机器 content={},加密后entryctContent={}", version, entryctContent);
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                            (byte) 0xCC));

            log.info("【协议结束 清除机器累计金额 给机器】");
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
             unsigned char type;				 	 //
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
