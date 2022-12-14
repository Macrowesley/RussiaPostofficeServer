package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFmReqDTO;
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
     * ???????????????????????????ssh??????
     * @param acnum
     */
    public boolean openSshProtocol(String acnum) {
        try {
            //????????????
            /*try {
                String cmd = "cmd /c net start cygsshd";
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }*/


            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(acnum);
            //??????????????????
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /**
             *     typedef  struct{
             *         unsigned char length[4];				 //4?????????
             *         unsigned char type;				 	 //0xC1
             *         unsigned char  operateID[2];
             *         unsigned char content[?];             //??????????????? ????????????(3)
             *         unsigned char check;				     //?????????
             *         unsigned char tail;					 //0xD0
             *     }__attribute__((packed))ssh, *ssh;
             */
            //????????????
            String version = FebsConstant.FmVersion1;
   /*         String domain = "device.uprins.com";
            String domainPort = "9091";
            String sshPort = "22";
            String sshPwd = "GDPT2020lai";
            String content = version + domain + domainPort + sshPort + sshPwd;*/
            String entryctContent = AESUtils.encrypt(version, tempKey);

            //????????????
            wrieteToCustomer(ctx, getWriteContent(BaseTypeUtils.stringToByte(entryctContent, BaseTypeUtils.UTF8), (byte) 0xC1));
            log.info("???????????????????????????ssh??????");
            return true;
        } catch (Exception e) {
            log.error("???????????????????????????ssh??????????????????????????????" + e.getMessage());
            return false;
        }
    }

    /**
     * ???????????????????????????ssh??????
     * @param acnum
     */
    public boolean closeSshProtocol(String acnum) {
        try {

            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(acnum);
            //??????????????????
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /**
             *     typedef  struct{
             *         unsigned char length[4];				 //4?????????
             *         unsigned char type;				 	 //0xC2
             *         unsigned char  operateID[2];
             *         unsigned char content[?];             //??????????????? ????????????(3)
             *         unsigned char check;				     //?????????
             *         unsigned char tail;					 //0xD0
             *     }__attribute__((packed))ssh, *ssh;
             */
            //????????????
            String version = FebsConstant.FmVersion1;
            String entryctContent = AESUtils.encrypt(version, tempKey);

            //????????????
            wrieteToCustomer(ctx, getWriteContent(BaseTypeUtils.stringToByte(entryctContent, BaseTypeUtils.UTF8), (byte) 0xC2));
            log.info("???????????????????????????ssh??????");

            return true;
        } catch (Exception e) {
            log.error("???????????????????????????ssh??????????????????????????????" + e.getMessage());
            return false;
        } finally {
            //??????????????????
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
     * ??????????????????
     * @param acnum
     * @return
     */
    public boolean updateRemoteFileProtocol(RemoteFileDTO remoteFileDTO) {
        try {

            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(remoteFileDTO.getAcnum());
            //??????????????????
            String tempKey = tempKeyUtils.getTempKey(ctx);


            /*typedef  struct{
                unsigned char length[4];			 //4?????????
                unsigned char type;				 	 //0xC5
                unsigned char operateID[2];
                unsigned char content[?];            //??????????????? ????????????(3) + RemoteFileDTO???json
                unsigned char check;				 //?????????
                unsigned char tail;					 //0xD0
            }__attribute__((packed))updateFile, *updateFile;

            public class RemoteFileDTO {
                Long deviceId;
                String acnum;
                String remoteFilePath;
                String url;
                String md5;
            }*/

            //????????????
            String version = "001";
            //??????????????????????????????????????????????????????
            String filePath = (String) redisService.get(remoteFileDTO.getUrl());
            byte[] bytes = FileUtil.readBytes(filePath);

            String md5Str = ByteUtils.toHexAscii(MD5Util.md5(bytes)).toLowerCase();
            remoteFileDTO.setMd5(md5Str);
            String entryctContent = AESUtils.encrypt(version + JSON.toJSONString(remoteFileDTO), tempKey);
            //????????????
            wrieteToCustomer(ctx, getWriteContent(BaseTypeUtils.stringToByte(entryctContent, BaseTypeUtils.UTF8), (byte) 0xC5));
            log.info("??????????????????????????????????????? " + remoteFileDTO.getRemoteFilePath());

//            redisService.del(remoteFileDTO.getUrl());
            return true;
        } catch (Exception e) {
            log.error("?????????????????????????????????????????????????????????" + e.getMessage());
            return false;
        }
    }


    /**
     * ???????????????????????????
     * @param frankMachineId
     * @param changeStatusRequestDTO
     */
    @Async(FebsConstant.ASYNC_POOL)
    public void changeStatus(String frankMachineId, ChangeStatusRequestDTO changeStatusRequestDTO) {

        try {
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            //??????????????????
            String tempKey = tempKeyUtils.getTempKey(ctx);

            /**
             typedef  struct{
                 unsigned char length[4];				 //4?????????
                 unsigned char type;				 //0xC3
                 unsigned char  operateID[2];
                 unsigned char version[3];			 //????????????(3)
                 unsigned char content[?];           //???????????????   StatusDTO???json ????????????frankMachineId  + status+ postOffice
                 unsigned char check;				 //?????????
                 unsigned char tail;				 //0xD0
             }__attribute__((packed))status, *status;
             **/
            //????????????
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
            log.info("????????????????????????????????? content={},?????????entryctContent={}", content, entryctContent);
            //????????????
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                    (byte) 0xC3));
        }catch (Exception e){
            log.error("???????????????????????????" + e.getMessage());
            //?????????
            throw new FmException(e.getMessage());
        }
    }

    /**
     * ??????privateKey?????????
     */
    @Async(FebsConstant.ASYNC_POOL)
    public void noticeMachineUpdateKey(String frankMachineId, PublicKey dbPublicKey){
        try {
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            //??????????????????
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //????????????
            String version = FebsConstant.FmVersion1;

            /**
             typedef  struct{
                 unsigned char length[4];			 //4?????????
                 unsigned char type;				 	 //0xC6
                 unsigned char  operateID[2];
                 unsigned char version[3];			 //????????????(3)
                 unsigned char content[?];            //???????????????   Key revision(4???????????????0??????)
                 unsigned char check;				 //?????????
                 unsigned char tail;					 //0xD0
             }__attribute__((packed))privateKey, *privateKey;
             */
            String content = dbPublicKey.getSuccessMsg();
            String entryctContent = AESUtils.encrypt(content, tempKey);
            log.info("???????????????????????????key content={},?????????entryctContent={}", content, entryctContent);
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                            (byte) 0xC6));
        } catch (Exception e) {
            throw new FmException(e.getMessage());
        }
    }

    /**
     * ??????tax?????????????????????
     */
    public void sendTaxToAllMachine(TaxVersionDTO taxVersionDTO, String jsonFileName){

        log.info("??????????????????????????????");
        List<Device> deviceList = deviceService.list();
        List<TaxDeviceUnreceived> taxDeviceUnreceivedList = new ArrayList<>();
        deviceList.stream().forEach(device -> {
            TaxDeviceUnreceived temp = new TaxDeviceUnreceived();
            temp.setFrankMachineId(device.getFrankMachineId());
            temp.setTaxVersion(taxVersionDTO.getVersion());
            temp.setCreatedTime(new Date());
            taxDeviceUnreceivedList.add(temp);
        });
        //???????????????????????????
        log.info("taxDeviceUnreceivedList.size() = " + taxDeviceUnreceivedList.size());
        taxDeviceUnreceivedService.saveUniqueBatch(taxDeviceUnreceivedList);

        //????????????????????????????????????????????????
        log.info("????????????tax??????????????????");
        ConcurrentHashMap<String, ChannelHandlerContext> loginChannelMap = channelMapperManager.getLoginChannelMap();
        Iterator<Map.Entry<String, ChannelHandlerContext>> entries = loginChannelMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, ChannelHandlerContext> entry = entries.next();

            //??????
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
     * ????????????tax???????????????
     * @param taxVersionDTO
     */
    @Async(FebsConstant.NETTY_ASYNC_POOL)
    public void sendTaxesToMachine(String frankMachineId, ChannelHandlerContext ctx, String taxVersion, Date applyDate, String taxJson, String jsonFileName, boolean isNeedSaveToDb) {
        String applyDateStr = DateKit.formatDateYmdhms(applyDate);
        log.info("??????????????? ?????????" + frankMachineId + "??????tax?????????, applyDate = "  + applyDateStr);
        /**
         *
         ??????tax??????
         typedef  struct{
         unsigned char length[4];				 //4?????????
         unsigned char type;				 	 //0xC4
         unsigned char  operateID[2];
         unsigned char version[3];			  //????????????(3)
         unsigned char content[?];            //??????????????? yyyymmddhhmmss(14) + md5(32) + url(56)http://russia.uprins.com:90/tax/2021_07_06_16_13_47.json
         unsigned char check;				 //?????????
         unsigned char tail;					 //0xD0
         }__attribute__((packed))sendTaxes, *sendTaxes;
         */
        try {
            if (ctx == null) {
                ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            }
            //??????????????????
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //????????????
            String version = FebsConstant.FmVersion1;

            String md5Str = MD5Util.MD5Encode(taxJson).toLowerCase();
            String url = downLoadBaseUrl + "tax/" + jsonFileName + ".json";
            String content = applyDateStr + md5Str + url;
//            String entryctContent = AESUtils.encrypt(content, tempKey);
//            log.info("???????????????tax????????? content={},?????????entryctContent={}", content, entryctContent);
            log.info("???????????????tax????????? applyDateStr={} md5 = {}, tax??????={}",applyDateStr, md5Str, taxJson.length() );
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + content, BaseTypeUtils.UTF8),
                            (byte) 0xC4));

            //?????????????????????????????????????????????????????????
            if (isNeedSaveToDb){
                TaxDeviceUnreceived temp = new TaxDeviceUnreceived();
                temp.setFrankMachineId(frankMachineId);
                temp.setTaxVersion(taxVersion);
                temp.setCreatedTime(new Date());
                taxDeviceUnreceivedService.saveOneUnique(temp);
            }
            log.info("??????????????? ??????tax????????????");
        } catch (Exception e) {
            throw new FmException(e.getMessage());
        }
    }



    /**
     * PC???????????????????????????,??????foreseen
     * ???????????????????????????
     */
    public void doPrintJob(PrintJob dbPrintJob) {
        try {
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(dbPrintJob.getFrankMachineId()));

            if (ctx == null) {
                throw new FebsException("??????" + dbPrintJob.getFrankMachineId() + "???????????????????????????");
            }


            FlowDetailEnum dbFlowDetail = FlowDetailEnum.getByCode(dbPrintJob.getFlowDetail());
            byte[] data = null;
            PrintProgressInfo productPrintProgress = printJobService.getProductPrintProgress(dbPrintJob);
            //??????foreseen????????????
            if (dbFlowDetail == FlowDetailEnum.JobingPcCreatePrint || dbFlowDetail == FlowDetailEnum.JobingErrorForeseensUnKnow){
                //??????????????????????????????????????????foreseen????????????????????????????????????Foreseen???????????????????????????????????????????????????????????????????????????
                Device dbDevice = deviceService.checkAndGetDeviceByFrankMachineId(dbPrintJob.getFrankMachineId());

                ForeseenFmReqDTO foreseenFmDto = new ForeseenFmReqDTO();
                foreseenFmDto.setContractCode(dbPrintJob.getContractCode());
                foreseenFmDto.setFrankMachineId(dbPrintJob.getFrankMachineId());
                foreseenFmDto.setUserId(FebsUtil.getCurrentUser().getUsername());
                foreseenFmDto.setPostOffice(dbDevice.getPostOffice());
                foreseenFmDto.setTotalCount(dbPrintJob.getTotalCount());

                //??????????????????foreseen????????????????????????????????????????????????????????????
//                foreseenFmDto.setProducts();

                foreseenFmDto.setTaxVersion(dbDevice.getTaxVersion());
                foreseenFmDto.setTotalAmmount(String.valueOf(MoneyUtils.changeY2F(dbPrintJob.getTotalAmount())));
                foreseenFmDto.setMachineDate(DateUtil.getCurTime());
                foreseenFmDto.setPrintJobType(dbPrintJob.getType());
                foreseenFmDto.setPrintJobId(dbPrintJob.getId());

                //???????????????????????????
                data = serviceManageCenter.foreseens(foreseenFmDto, dbPrintJob, ctx, productPrintProgress);
            }else{
                //?????????????????????foreseen,??????transaction????????????????????????????????????????????????????????????????????????
                Contract dbContract = checkUtils.checkContractIsOk(dbPrintJob.getContractCode());
                data = serviceManageCenter.buildForeseenResultBytes(dbPrintJob,ctx, dbPrintJob.getForeseenId(), dbContract, productPrintProgress);
            }

            wrieteToCustomer(ctx, getWriteContent(data, ForeseensPortocol.PROTOCOL_TYPE));

            msgService.sendMsg(WebSocketEnum.ClickPrintRes.getCode(), dbPrintJob.getId(),"");
            log.info("??????????????? ??????pc??????????????????");
        } catch (Exception e) {
//            e.printStackTrace();
            throw new FmException(e.getMessage());
        }
    }



    /**
     * PC????????????????????????
     */
    public void cancelPrintJob(PrintJob dbPrintJob) {
        /*typedef  struct{
            unsigned char length[4];
            unsigned char type;				 	 //0xC8
            unsigned char operateID[2];
            unsigned char version[3];			 //????????????(3)
            unsigned char content[?];            //???????????????: pcCancelInfoDTO??????
            unsigned char check;				 //?????????
            unsigned char tail;					 //0xD0
        }__attribute__((packed))pcPrintJobCancel, *pcPrintJobCancel;

        */

        /**
         * ????????????????????????????????????
         */
        try {
            log.info("??????????????? ??????pc ?????????????????? ????????????");
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(dbPrintJob.getFrankMachineId()));
            if (ctx == null) {
                throw new FebsException("??????" + dbPrintJob.getFrankMachineId() + "???????????????????????????");
            }
            //??????????????????
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //????????????
            String version = FebsConstant.FmVersion1;

            String content = JSON.toJSONString(
                    new PcCancelInfoDTO(
                            String.valueOf(FebsUtil.getCurrentUser().getUserId()),
                            String.valueOf(dbPrintJob.getId()),
                            dbPrintJob.getForeseenId() == null ? "" : dbPrintJob.getForeseenId()));
            String entryctContent = AESUtils.encrypt(content, tempKey);
            log.info("????????? ??????pc ?????????????????? ????????? content={},?????????entryctContent={}", content, entryctContent);
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                            (byte) 0xC8));

            msgService.sendMsg(WebSocketEnum.CancelPrintRes.getCode(), dbPrintJob.getId(),"");
            log.info("??????????????? ??????pc ?????????????????? ????????????");
        } catch (Exception e) {
            throw new FmException(e.getMessage());
        }
    }

    /**
     * 1. ?????????????????????????????????
     * ???????????????
     * - ????????????????????????????????????????????????
     * - PC?????????????????????????????????????????????????????????
     * @param frankMachineId
     */
    public void syncImageList(String frankMachineId, AdImageInfo[] adImageInfoArr){
        /*typedef  struct{
            unsigned char length[4];
            unsigned char type;				 	 //0xCA
            unsigned char operateID[2];
            unsigned char version[3];			 //????????????(3)
            unsigned char content[?];            //???????????????: AdInfoDTO
            unsigned char check;				 //?????????
            unsigned char tail;					 //0xD0
        }__attribute__((packed))adInfo, *adInfo;*/

        try {
            log.info("??????????????? ???????????????????????? ????????????");
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            if (ctx == null) {
                throw new FebsException("??????" + frankMachineId + "???????????????????????????");
            }
            //??????????????????
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //????????????
            String version = FebsConstant.FmVersion1;


            AdInfoDTO adInfoDTO = new AdInfoDTO();
            adInfoDTO.setAdImageList(adImageInfoArr);
            String content = JSON.toJSONString(adInfoDTO);
            String entryctContent = AESUtils.encrypt(content, tempKey);
            log.info("????????? ???????????????????????? ????????? content={},?????????entryctContent={}", content, entryctContent);
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                            (byte) 0xCA));

            log.info("??????????????? ???????????????????????? ????????????");
        } catch (Exception e) {
            throw new FmException(e.getMessage());
        }
    }

    /**
     * ????????????????????????
     * @param frankMachineId
     */
    public void clearFmMoney(String frankMachineId){
        /*typedef  struct{
            unsigned char length[4];			 //4?????????
            unsigned char type;				 	 //0xCC
            unsigned char operateID[2];
            unsigned char content[?];            //??????????????? ????????????(3)
            unsigned char check;				 //?????????
            unsigned char tail;					 //0xD0
        }__attribute__((packed))clearMoney, *clearMoney;*/


        try {
            log.info("??????????????? ???????????????????????? ????????????");
            ChannelHandlerContext ctx = channelMapperManager.getChannelByAcnum(getAcnumByFmId(frankMachineId));
            if (ctx == null) {
                throw new FebsException("??????" + frankMachineId + "???????????????????????????");
            }
            //??????????????????
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //????????????
            String version = FebsConstant.FmVersion1;

            String entryctContent = AESUtils.encrypt(version, tempKey);

            log.info("????????? ???????????????????????? ????????? content={},?????????entryctContent={}", version, entryctContent);
            wrieteToCustomer(
                    ctx,
                    getWriteContent(BaseTypeUtils.stringToByte(version + entryctContent, BaseTypeUtils.UTF8),
                            (byte) 0xCC));

            log.info("??????????????? ???????????????????????? ????????????");
        } catch (Exception e) {
            throw new FmException(e.getMessage());
        }
    }


     /*
     ***********************************************************
     ???????????????
     ************************************************************
     */
    /**
     * ????????????
     * @param frankMachineId
     * @param managerBalanceDTO
     */
    public void balance(String frankMachineId, ManagerBalanceDTO managerBalanceDTO) {
        /**
         typedef  struct{
             unsigned char length[4];				 //4?????????
             unsigned char type;				 	 //
             unsigned char  operateID[2];
             unsigned char content[?];            //??????????????? ????????????(3) + frankMachineId() + balanceId(?) + contractCode(?) + contractNum(?) + current(?) + consolidate(?)
             unsigned char check;				 //?????????
             unsigned char tail;					 //0xD0
         }__attribute__((packed))updateTaxes, *updateTaxes;
         */

        //todo ????????????????????????

    }

    /*
     ***********************************************************
     ????????????
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
