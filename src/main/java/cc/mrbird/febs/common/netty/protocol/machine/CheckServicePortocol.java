package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CheckServiceDTO;
import cc.mrbird.febs.common.netty.protocol.dto.CheckServiceResultDTO;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionMsgFMDTO;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.api.CheckUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.enums.InformRussiaEnum;
import cc.mrbird.febs.rcs.common.enums.PrintJobTypeEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.entity.Tax;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import cc.mrbird.febs.rcs.service.IPublicKeyService;
import cc.mrbird.febs.rcs.service.ITaxService;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;


@Slf4j
@Component
public class CheckServicePortocol extends MachineToServiceProtocol {
    @Autowired
    RedisService redisService;

    public static final byte PROTOCOL_TYPE = (byte) 0xB3;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //taxVersion长度
    private static final int TAX_VERSION_LEN = 10;

    private static final String OPERATION_NAME = "CheckServicePortocol";

    @Autowired
    IPrintJobService printJobService;

    @Autowired
    IDeviceService deviceService;

    @Autowired
    IPublicKeyService publicKeyService;

    @Autowired
    ITaxService taxService;

    @Autowired
    ITransactionMsgService dmMsgService;

    public static CheckServicePortocol checkServicePortocol;

    @PostConstruct
    public void init(){
        checkServicePortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return checkServicePortocol;
    }

    @Autowired
    CheckUtils checkUtils;
    /**
     * 获取协议类型
     *
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    @Override
    public String getProtocolName() {
        return OPERATION_NAME;
    }

    /**
     * 解析并返回结果流
     *
     * @param bytes
     * @param ctx
     * @return
     */
    @Override
    public byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        String version = null;
        try {
        /*
            作用：
             1. 请求服务器返回最新状态
             2. 返回上一次打印任务信息

            typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[4];			//
                unsigned char type;					//0xB3
                unsigned char  operateID[2];
                unsigned char acnum[6];             //机器表头号
                unsigned char version[3];           //版本号
                unsigned char content[?];			//加密后内容: CheckServiceDTO 对象的json
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))CheckService, *CheckService;
         */
            if (!FebsConstant.IS_TEST_NETTY) {
                //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
                String key = ctx.channel().id().toString() + "_" + OPERATION_NAME;
                if (checkServicePortocol.redisService.hasKey(key)) {
                    return getOverTimeResult(version, ctx, key, FMResultEnum.Overtime.getCode());
                } else {
                    log.info("channelId={}的操作记录放入redis", key);
                    checkServicePortocol.redisService.set(key, "wait", WAIT_TIME);
                }
            }

            int pos = getBeginPos();

            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //版本号
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;

            switch (version) {
                case FebsConstant.FmVersion1:


                    /**
                     typedef  struct{
                     unsigned char length[4];				 //2个字节
                     unsigned char type;				 	 //0xB3
                     unsigned char  operateID[2];
                     unsigned char content;				     //加密内容:
                                                                        result(长度为2 1 成功)
                                                                        + version
                                                                        + 机器状态code(1)

                                                                        + 订单是否结束（1 结束 0 未结束）
                                                                        + 机器的私钥是否需要更新（0 不需要更新 1需要更新）
                                                                        + 机器的税率是否需要更新（0不需要 1需要更新）
                                                                        + ForeseenFMDTO 的Json
                     unsigned char check;				     //校验位
                     unsigned char tail;					 //0xD0
                     }__attribute__((packed))CheckServiceResult, *CheckServiceResult;
                     */

                    //获取上次打印任务信息
//                    String decryptContent = getDecryptContent(bytes, ctx, pos, REQ_ACNUM_LEN);
                    CheckServiceDTO checkServiceDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, CheckServiceDTO.class);
                    log.info("checkServiceDTO =" + checkServiceDTO.toString());
                    String taxVersion = checkServiceDTO.getTaxVersion().trim();
                    log.info("fmTaxVersion = " + taxVersion);
                    if (StringUtils.isEmpty(taxVersion)
                            || StringUtils.isEmpty(checkServiceDTO.getFrankMachineId())
                            || checkServiceDTO.getDmMsgDto() == null) {
                        return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.SomeInfoIsEmpty.getCode());
                    }

                    String frankMachineId = checkServiceDTO.getFrankMachineId().trim();
                    if (!checkServicePortocol.deviceService.checkExistByFmId(frankMachineId)) {
                        return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DeviceNotFind.getCode());
                    }

                    Date machineDate = DateKit.parseDateYmdhms(checkServiceDTO.getMachineDate());

                    //请求服务器返回最新状态
                    Device dbDevice = checkServicePortocol.deviceService.checkAndGetDeviceByFrankMachineId(frankMachineId);

                    //判断是否需要通知俄罗斯/rateTables
                    Tax lastestTax = checkServicePortocol.taxService.getLastestTax();
                    if (lastestTax != null && lastestTax.getInformRussia().equals(InformRussiaEnum.NO.getCode())){
                        //服务器没有成功通知俄罗斯/rateTables 再次尝试
                        log.info("服务器没有成功通知俄罗斯/rateTables 再次尝试");
                        if(!checkServicePortocol.serviceManageCenter.rateTables(lastestTax.getVersion())){
                            log.info("rateTables fail");
                            throw new FmException(FMResultEnum.RateTablesFail.getCode(), "rateTables fail ");
                        }
                    }

                    /**
                     * 校验机器tax是否需要更新
                     *
                     */
                    checkServicePortocol.checkUtils.checkTaxIsOk(frankMachineId, ctx, taxVersion, checkServiceDTO.getMachineDate());

                    //机器的税率是否需要更新（0不需要 1需要更新）
                    /*int isFmTaxNeedUpdate = 0;
                    if (lastestTax != null && fmTaxVersion.equals(lastestTax.getVersion())){
                        log.info("开始判断是否需要通知俄罗斯");
                        //服务器已经成功通知俄罗斯/rateTables
                        //机器已经更新了tax,需要处理下数据库的状态了
                        //tax是否更新 默认为1 最新状态  0 没有更新到最新状态
                        Integer deviceTaxIsUpdate = dbDevice.getTaxIsUpdate();
                        if (deviceTaxIsUpdate == TaxUpdateEnum.NOT_UPDATE.getCode()) {
                            //机器已经更新了tax，但是数据库的device没有更新状态，更新数据库机器状态
                            log.info("调用俄罗斯frankMachines接口，更新数据库");
                            dbDevice.setTaxVersion(fmTaxVersion);
                            checkServicePortocol.serviceManageCenter.frankMachinesRateTableUpdateEvent(dbDevice);
                        } else {
                            //机器更新了tax,数据库也更新了device的tax信息，不处理
                            log.info("机器更新了tax,数据库也更新了device的tax信息，不处理");
                        }

                    }else{
                        //机器没有更新tax，需要更新
                        log.info("机器没有更新tax，需要更新");
                        isFmTaxNeedUpdate = 1;
                    }*/

                    //校验数据库的私钥是否完成闭环 机器的私钥是否需要更新（0 不需要更新 1需要更新）
                    int isFmPrivateNeedUpdate = checkServicePortocol.publicKeyService.checkFmIsUpdate(dbDevice.getFrankMachineId()) ? 0 : 1;

                    //数据库的合同信息
                    ForeseenFMDTO foreseenFMDTO = new ForeseenFMDTO();
                    PrintJob dbPrintJob = checkServicePortocol.printJobService.getLastestJobByFmId(frankMachineId, PrintJobTypeEnum.Machine.getCode());
                    String transactionId = "";
                    String printJobType = "";
                    boolean isPrintEnd = true;
                    if (dbPrintJob != null) {
                        transactionId = dbPrintJob.getTransactionId();
                        printJobType = String.valueOf(dbPrintJob.getType());
                        //订单是否结束（1 结束 0 未结束）
                        isPrintEnd = dbPrintJob.getFlow() == FlowEnum.FlowEnd.getCode();
                        String foreseenId = dbPrintJob.getForeseenId();
                        if (!isPrintEnd && StringUtils.isNotEmpty(foreseenId)) {
                            //没有闭环，返回foreseen信息
                            Foreseen dbForeseen = checkServicePortocol.printJobService.getForeseenById(foreseenId);
                            if (dbForeseen != null) {
                                BeanUtils.copyProperties(dbForeseen, foreseenFMDTO);
                                foreseenFMDTO.setTotalAmmount(String.valueOf(MoneyUtils.changeY2F(dbForeseen.getTotalAmmount())));
                            }
                        }

                        /**
                         * 是否打印完成
                         * 		是
                         * 			不发送额外协议
                         * 		否
                         * 			发送打印信息协议
                         */
                        /*if (!dbPrintJob.getFlow().equals(FlowEnum.FlowEnd.getCode()) && dbPrintJob.getType() == PrintJobTypeEnum.Web.getCode()){
                            log.info("开机校验中 PC订单没有打印完成，发送打印进度");
                            checkServicePortocol.serviceManageCenter.doPrintJob(dbPrintJob);
                        }
*/
                    }
                    log.info("构建foreseenFMDTO信息");

                    /**
                     * 【处理dmMsg信息】
                     * 开机启动的时候，根据订单是否结束判断是否需要存入数据库
                     *    订单结束，不处理
                     *    订单未结束，判断上一个批次是否结束
                     *      结束了，不处理
                     *      未结束：找到没有结束的那个批次，得到dm_msg等信息，保存到数据库中，同时返回这个批次打印的count等信息给机器
                     */
                    TransactionMsgFMDTO transactionMsgFMDTO = checkServiceDTO.getDmMsgDto();
                    //当前任务已经打印的总数量
                    int actualCount = 0;
                    //当前任务已经打印的总金额 单位是分
                    String actualAmount = "0";
                    //二维码内容（不包含签名）
//                    String dmMsg = "";
                    //如果没有打印结束而且，transactionid已经创建了，就不处理
                    if (!isPrintEnd && !StringUtils.isEmpty(transactionId)){
                        DmMsgDetail dmMsgDetail = checkServicePortocol.dmMsgService.getDmMsgDetailOnFmStart(transactionId, transactionMsgFMDTO);
                        if (dmMsgDetail != null){
                            actualCount = dmMsgDetail.getActualCount();
                            actualAmount = dmMsgDetail.getActualAmount();
//                            FrankDTO[] franks = dmMsgDetail.getFranks();
//                            dmMsg = franks.length > 0 ? franks[franks.length - 1].getDmMessage() : "";
                        }
                    }
                    log.info("处理dmMsg信息");

                    //拼接返回信息
                    CheckServiceResultDTO resultDto = new CheckServiceResultDTO();
                    resultDto.setResult(FMResultEnum.SUCCESS.getSuccessCode());
                    resultDto.setVersion(version);
                    resultDto.setFmStatus(String.valueOf(dbDevice.getCurFmStatus()));
                    resultDto.setFutureStatus(String.valueOf(dbDevice.getFutureFmStatus()));
                    resultDto.setIsFmNeedChangeStatus(dbDevice.getFlow() == 1 ? "0" : "1");
                    resultDto.setIsRussia(String.valueOf(dbDevice.getIsRussia()));
                    resultDto.setIsPrintEnd(String.valueOf(isPrintEnd == true ? 1 : 0));
                    resultDto.setIsFmPrivateNeedUpdate(String.valueOf(isFmPrivateNeedUpdate));
//                    resultDto.setIsFmTaxNeedUpdate(String.valueOf(isFmTaxNeedUpdate));
                    resultDto.setActualCount(String.valueOf(actualCount));
                    resultDto.setActualAmount(actualAmount);
//                    resultDto.setDmMsg(dmMsg);
                    resultDto.setServerDate(DateKit.formatDateYmdhms(new Date()));
                    resultDto.setTransactionId(transactionId);
                    resultDto.setPrintJobType(printJobType);
//                    resultDto.setForeseenFMDTO(JSON.toJSONString(foreseenFMDTO, SerializerFeature.DisableCircularReferenceDetect));
                    BeanUtils.copyProperties(foreseenFMDTO, resultDto);
                    resultDto.setForeseenId(foreseenFMDTO.getId());

                    String responseData = JSON.toJSONString(resultDto, SerializerFeature.DisableCircularReferenceDetect);

                    /*String responseData =
                            FMResultEnum.SUCCESS.getSuccessCode()
                                    + version
                                    + String.valueOf(curStatus)
                                    + (isPrintEnd == true ? 1 : 0)
                                    + String.valueOf(isFmPrivateNeedUpdate)
                                    + String.valueOf(isFmTaxNeedUpdate)
                                    + JSON.toJSONString(foreseenFMDTO);*/
                    String tempKey = checkServicePortocol.tempKeyUtils.getTempKey(ctx);
                    String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
                    log.info(OPERATION_NAME + "协议：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);


                    //返回打印协议
                    return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
                default:
                    return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }

        } catch (FmException e) {
            e.printStackTrace();
            log.error(OPERATION_NAME + " FmException info = " + e.getMessage());
            if (-1 != e.getCode()) {
                return getErrorResult(ctx, version, OPERATION_NAME, e.getCode());
            } else {
                return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(OPERATION_NAME + "error info = " + e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
        } finally {
        }

    }

}
