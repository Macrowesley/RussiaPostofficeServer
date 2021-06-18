package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CheckServiceDTO;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFMDTO;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.api.ServiceManageCenter;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.enums.TaxUpdateEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.entity.Tax;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import cc.mrbird.febs.rcs.service.IPublicKeyService;
import cc.mrbird.febs.rcs.service.ITaxService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


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
    ServiceManageCenter serviceManageCenter;

    @Autowired
    ITaxService taxService;

    public static CheckServicePortocol checkServicePortocol;

    @PostConstruct
    public void init(){
        this.checkServicePortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return checkServicePortocol;
    }


    /**
     * 获取协议类型
     *
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    /**
     * 解析并返回结果流
     *
     * @param bytes
     * @param ctx
     * @return
     */
    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        String version = null;
        try {
        /*
            作用：
             1. 请求服务器返回最新状态
             2. 返回上一次打印任务信息

            typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[2];			//
                unsigned char type;					//0xB3
                unsigned char acnum[6];             //机器表头号
                unsigned char version[3];           //版本号
                unsigned char content[?];			//加密后内容: CheckServiceDTO 对象的json
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))CheckService, *CheckService;
         */
            log.info("机器开始 {}", OPERATION_NAME);

            //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
            String key = ctx.channel().id().toString() + "_" + OPERATION_NAME;
            if (redisService.hasKey(key)) {
                return getOverTimeResult(version, ctx, key, FMResultEnum.Overtime.getCode());
            } else {
                log.info("channelId={}的操作记录放入redis", key);
                redisService.set(key, "wait", WAIT_TIME);
            }


            int pos = TYPE_LEN;

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
                     unsigned char length[2];				 //2个字节
                     unsigned char head;				 	 //0xB3
                     unsigned char content;				     //加密内容: result(长度为2 1 成功) + version + 机器状态code(1) + 订单是否结束（1 结束 0 未结束）+ 机器的私钥是否需要更新（0 不需要更新 1需要更新） + 机器的税率是否需要更新（0不需要 1需要更新） + ForeseenFMDTO 的Json
                     unsigned char check;				     //校验位
                     unsigned char tail;					 //0xD0
                     }__attribute__((packed))CheckServiceResult, *CheckServiceResult;
                     */

                    //获取上次打印任务信息
//                    String decryptContent = getDecryptContent(bytes, ctx, pos, REQ_ACNUM_LEN);
                    CheckServiceDTO checkServiceDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, CheckServiceDTO.class);
                    String frankMachineId = checkServiceDTO.getFrankMachineId().trim();
                    String fmTaxVersion = checkServiceDTO.getTaxVersion().trim();

                    //请求服务器返回最新状态
                    Device dbDevice = checkServicePortocol.deviceService.findDeviceByAcnum(acnum);
                    int curStatus = dbDevice.getCurFmStatus();

                    /**
                     * 校验机器tax是否需要更新
                     */
                    //机器的税率是否需要更新（0不需要 1需要更新）
                    int isFmTaxNeedUpdate = 0;
                    Tax lastestTax = checkServicePortocol.taxService.getLastestTax();
                    if (fmTaxVersion.equals(lastestTax.getVersion())){
                        //机器已经更新了tax,需要处理下数据库的状态了
                        //tax是否更新 默认为1 最新状态  0 没有更新到最新状态
                        Integer deviceTaxIsUpdate = dbDevice.getTaxIsUpdate();
                        if (deviceTaxIsUpdate == TaxUpdateEnum.NOT_UPDATE.getCode()){
                            //机器已经更新了tax，但是数据库的device没有更新状态，更新数据库机器状态
                            checkServicePortocol.serviceManageCenter.rateTableUpdateEvent(dbDevice);
                        }else{
                            //机器更新了tax,数据库也更新了device的tax信息，不处理
                        }
                    }else{
                        //机器没有更新tax，需要更新
                        isFmTaxNeedUpdate = 1;
                    }

                    //校验数据库的私钥是否完成闭环 机器的私钥是否需要更新（0 不需要更新 1需要更新）
                    int isFmPrivateNeedUpdate = checkServicePortocol.publicKeyService.checkFmIsUpdate(dbDevice.getFrankMachineId()) ? 0 : 1;

                    //数据库的合同信息
                    PrintJob dbPrintJob = checkServicePortocol.printJobService.getLastestJobByFmId(frankMachineId);
                    //订单是否结束（1 结束 0 未结束）
                    boolean isPrintEnd = dbPrintJob.getFlow() == FlowEnum.FlowEnd.getCode();
                    ForeseenFMDTO foreseenFMDTO = new ForeseenFMDTO();
                    if (!isPrintEnd) {
                        //没有闭环，返回foreseen信息  问问小刘 需不需要细节？ 需不需要transaction信息
                        String foreseenId = dbPrintJob.getForeseenId();
                        Foreseen dbForeseen = checkServicePortocol.printJobService.getForeseenById(foreseenId);

                        BeanUtils.copyProperties(dbForeseen, foreseenFMDTO);
                        foreseenFMDTO.setTotalAmmount(String.valueOf(MoneyUtils.changeY2F(dbForeseen.getTotalAmmount())));
                    }

                    //拼接返回信息
                    String responseData =
                            FMResultEnum.SUCCESS.getSuccessCode()
                                    + version
                                    + String.valueOf(curStatus)
                                    + (isPrintEnd == true ? 1 : 0)
                                    + String.valueOf(isFmPrivateNeedUpdate)
                                    + String.valueOf(isFmTaxNeedUpdate)
                                    + JSON.toJSONString(foreseenFMDTO);
                    String tempKey = checkServicePortocol.tempKeyUtils.getTempKey(ctx);
                    String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
                    log.info("foreseens协议：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
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
            log.error(OPERATION_NAME + "error info = " + e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
        } finally {
            log.info("机器结束 Foreseens");
        }

    }

}
