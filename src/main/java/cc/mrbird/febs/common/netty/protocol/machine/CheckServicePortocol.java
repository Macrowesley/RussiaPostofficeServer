package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFMDTO;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import cc.mrbird.febs.rcs.service.IPublicKeyService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CheckServicePortocol extends MachineToServiceProtocol {
    @Autowired
    RedisService redisService;

    public static final byte PROTOCOL_TYPE = (byte) 0xB5;

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
                unsigned char type;					//0xB8
                unsigned char acnum[6];             //机器表头号
                unsigned char version[3];           //版本号
                unsigned char content[?];			//加密后内容: FrankMachineId(36)
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
                     unsigned char head;				 	 //0xB8
                     unsigned char content;				     //加密内容: result(1 成功) + version + statuscode(1) + 订单是否结束（1 结束 0 未结束）+ 私钥是否更新（0未更新 1更新） + 税率是否更新（0未更新 1更新了） + ForeseenFMDTO 的Json
                     unsigned char check;				     //校验位
                     unsigned char tail;					 //0xD0
                     }__attribute__((packed))CheckServiceResult, *CheckServiceResult;
                     */

                    //请求服务器返回最新状态
                    Device dbDevice = deviceService.findDeviceByAcnum(acnum);
                    int curStatus = dbDevice.getCurFmStatus();

                    //校验机器tax是否更新
                    int isFmTaxUpdate = dbDevice.getTaxIsUpdate();

                    //校验机器私钥是否更新
                    int isFmPrivateUpdate = publicKeyService.checkFmIsUpdate(dbDevice.getFrankMachineId()) ? 1 : 0;

                    //获取上次打印任务信息
                    String decryptContent = getDecryptContent(bytes, ctx, pos, REQ_ACNUM_LEN);
                    String frankMachineId = decryptContent.trim();

                    //数据库的合同信息
                    PrintJob dbPrintJob = printJobService.getLastestJobByFmId(frankMachineId);
                    boolean isPrintEnd = dbPrintJob.getFlow() == FlowEnum.FlowEnd.getCode();
                    ForeseenFMDTO foreseenFMDTO = new ForeseenFMDTO();
                    if (!isPrintEnd) {
                        //没有闭环，返回foreseen信息  问问小刘 需不需要细节？ 需不需要transaction信息
                        String foreseenId = dbPrintJob.getForeseenId();
                        Foreseen dbForeseen = printJobService.getForeseenById(foreseenId);

                        BeanUtils.copyProperties(dbForeseen, foreseenFMDTO);
                        foreseenFMDTO.setTotalAmmount(String.valueOf(MoneyUtils.changeY2F(dbForeseen.getTotalAmmount())));
                    }

                    //拼接返回信息
                    String responseData =
                            FMResultEnum.SUCCESS.getSuccessCode()
                                    + version
                                    + String.valueOf(curStatus)
                                    + (isPrintEnd == true ? 1 : 0)
                                    + String.valueOf(isFmPrivateUpdate)
                                    + String.valueOf(isFmTaxUpdate)
                                    + JSON.toJSONString(foreseenFMDTO);
                    String tempKey = tempKeyUtils.getTempKey(ctx);
                    String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
                    log.info("foreseens协议：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
                    return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
                default:
                    return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }
        } catch (Exception e) {
            log.error(OPERATION_NAME + "error info = " + e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
        } finally {
            log.info("机器结束 Foreseens");
        }

    }

}
