package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.entity.Contract;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@NoArgsConstructor
@Component
@Deprecated
public class ForeseensCancelPortocol extends MachineToServiceProtocol {
    @Autowired
    RedisService redisService;

    public static final byte PROTOCOL_TYPE = (byte) 0xB7;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;



    private static final String OPERATION_NAME = "ForeseensCancelPortocol";

    public static ForeseensCancelPortocol foreseensCancelPortocol;

    @PostConstruct
    public void init(){
        foreseensCancelPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return foreseensCancelPortocol;
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
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        String version = null;
        try {
        /*
        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length[4];			//
            unsigned char type;					//0xB7
            unsigned char  operateID[2];
            unsigned char acnum[6];             //机器表头号
            unsigned char version[3];           //版本号
            unsigned char content[?];			//加密后内容: CancelJobFMDTO
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))CancelJob, *CancelJob;
         */
            //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
            String key = ctx.channel().id().toString() + "_" + OPERATION_NAME;
            if (redisService.hasKey(key)) {
                return getOverTimeResult(version, ctx, key, FMResultEnum.Overtime.getCode());
            } else {
                log.info("channelId={}的操作记录放入redis", key);
                redisService.set(key, "wait", WAIT_TIME);
            }
            log.info("机器开始 ForeseensCancel");

            int pos = getBeginPos();

            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //版本号
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;

            switch (version) {
                case FebsConstant.FmVersion1:
                    CancelJobFMDTO cancelJobFMDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, CancelJobFMDTO.class);
                    log.info("解析得到的对象：cancelJobFMDTO={}", cancelJobFMDTO.toString());

                    //暂时不用这个协议了
//                    Contract dbContract = foreseensCancelPortocol.serviceManageCenter.cancelJob(cancelJobFMDTO);
//                    return getSuccessResult(version, ctx, cancelJobFMDTO, dbContract);
                    return null;
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
            return getErrorResult(ctx, version, OPERATION_NAME);
        } finally {
            log.info("机器结束 ForeseensCancelPortocol");
        }
    }

    private byte[] getSuccessResult(String version, ChannelHandlerContext ctx, CancelJobFMDTO cancelJobFMDTO, Contract contract) throws Exception {
        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char type;				 	 //0xB7
         unsigned char  operateID[2];
         unsigned char content;				     //加密内容: result(1 成功) + version + foreseenId（36）+ consolidate(8 分为单位) + current(8 分为单位)
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))CancelJobResult, *CancelJobResult;
         */
        String responseData = FMResultEnum.SUCCESS.getSuccessCode() + version + cancelJobFMDTO.getForeseenId() + MoneyUtils.changeY2F(contract.getConsolidate()) + MoneyUtils.changeY2F(contract.getCurrent());
        String tempKey = foreseensCancelPortocol.tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("CancelJob 协议：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }
}
