package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFMDTO;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ForeseensPortocol extends MachineToServiceProtocol {
    @Autowired
    RedisService redisService;

    public static final byte PROTOCOL_TYPE = (byte) 0xB5;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //返回数据长度
    private static final int RES_DATA_LEN = 1;

    private static final String OPERATION_NAME = "ForeseensPortocol";

    @Autowired
    IPrintJobService printJobService;

    /**
     * 获取协议类型
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
        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length[2];			//
            unsigned char type;					//0xB5
            unsigned char acnum[6];             //机器表头号
            unsigned char version[3];           //版本号
            unsigned char content[?];			//加密后内容: ForeseenFMDTO的json
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))Foreseens, *Foreseens;
         */

            log.info("机器开始 Foreseens");

            //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
            String key = ctx.channel().id().toString() + "_" + OPERATION_NAME;
            if (redisService.hasKey(key)){
                //todo 临时测试
                return getOverTimeResult(version,ctx, key, FMResultEnum.Overtime.getCode());
            }else{
                log.info("channelId={}的操作记录放入redis", key);
                redisService.set(key,"wait", WAIT_TIME);
            }


            int pos = TYPE_LEN;

            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;
            log.info("acnum = {}", acnum);

            //版本号
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;

            switch (version) {
                case FebsConstant.FmVersion1:
                    ForeseenFMDTO foreseenFMDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, ForeseenFMDTO.class);
                    log.info("解析得到的对象：foreseenFMDTO={}", foreseenFMDTO.toString());

                    //判断上一次打印是否闭环
                    PrintJob dbPrintJob = printJobService.getUnFinishJobByFmId(foreseenFMDTO.getFrankMachineId());
                    if (dbPrintJob != null) {
                        /**
                         * 特殊的情况：上次订单过程中，访问俄罗斯transaction接口时，没有访问成功，导致没有闭环，解决方案如下：
                         * 返回给机器一个状态，让机器直接再次发送transaction信息
                         */
                        FlowEnum dbFlow = FlowEnum.getByCode(dbPrintJob.getFlow());
                        FlowDetailEnum curFlowDetail = FlowDetailEnum.getByCode(dbPrintJob.getFlowDetail());

                        if (curFlowDetail == FlowDetailEnum.JobErrorTransactionUnKnow){
                            log.error("foreseens TransactionError异常  FrankMachineId = "+ dbPrintJob.getFrankMachineId()+" 的机器 访问俄罗斯transaction接口时，没有访问成功，ForeseenId = " + dbPrintJob.getForeseenId());
                            return getErrorResult(ctx, version,OPERATION_NAME, FMResultEnum.TransactionError.getCode());
                        }
                        //还未闭环，请等待
                        log.error("foreseens 上一次的打印任务没有闭环，进度为：" + FlowDetailEnum.getByCode(dbPrintJob.getFlowDetail()).getMsg());
                        return getErrorResult(ctx, version,OPERATION_NAME, FMResultEnum.NotFinish.getCode());
                    }

                    String foreseenId = AESUtils.createUUID();
                    foreseenFMDTO.setId(foreseenId);
                    //数据库的合同信息
                    Contract dbContract = serviceManageCenter.foreseens(foreseenFMDTO);

                    return getSuccessResult(version,ctx,foreseenId,dbContract);
                default:
                    return getErrorResult(ctx, version,OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }
        } catch (FmException e) {
            e.printStackTrace();
            log.error(OPERATION_NAME + " FmException info = " + e.getMessage());
            if (-1 != e.getCode()) {
                return getErrorResult(ctx, version, OPERATION_NAME, e.getCode());
            }else{
                return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(OPERATION_NAME + " error info = " + e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
        } finally {
            log.info("机器结束 Foreseens");
        }
    }

    private byte[] getSuccessResult(String version, ChannelHandlerContext ctx, String foreseenId, Contract contract) throws Exception{
        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char head;				 	 //0xB5
         unsigned char content;				     //加密内容: result(1 成功) + version + foreseenId（36）+ consolidate(8 分为单位) + current(8 分为单位)
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))ForeseensResult, *ForeseensResult;
         */
        String responseData = FMResultEnum.SUCCESS.getCode() + version + foreseenId + MoneyUtils.changeY2F(contract.getConsolidate()) + MoneyUtils.changeY2F(contract.getCurrent());
        String tempKey = tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("foreseens协议：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }
}
