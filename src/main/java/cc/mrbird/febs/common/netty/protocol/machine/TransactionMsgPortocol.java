package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionMsgFMDTO;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Slf4j
@NoArgsConstructor
@Component
public class TransactionMsgPortocol extends MachineToServiceProtocol {

    public static final byte PROTOCOL_TYPE = (byte) 0xBA;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    private static final String OPERATION_NAME = "TransactionMsgPortocol";

    public static TransactionMsgPortocol transactionMsgPortocol;

    @PostConstruct
    public void init(){
        transactionMsgPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return transactionMsgPortocol;
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
        long t1 = System.currentTimeMillis();
        try {
            /*
            typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[4];
                unsigned char type;					//0xBA
                unsigned char operateID[2];
                unsigned char acnum[6];             //机器表头号
                unsigned char version[3];           //版本号
                unsigned char content[?];			//加密后内容: TransactionMsgFMDTO 的json
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))TransactionMsg, *TransactionMsg;
             */
            log.info("机器开始 transactionMsg");

            if (!FebsConstant.IS_TEST_NETTY){
                //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
                String key = ctx.channel().id().toString() + "_" + OPERATION_NAME;
                if (transactionMsgPortocol.redisService.hasKey(key)) {
                    return getOverTimeResult(version, ctx, key, FMResultEnum.Overtime.getCode());
                } else {
                    log.info("channelId={}的操作记录放入redis", key);
                    transactionMsgPortocol.redisService.set(key, "wait", 2L);
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
                    TransactionMsgFMDTO transactionMsgFmDto = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, TransactionMsgFMDTO.class);

                    String transactionId = transactionMsgPortocol.serviceManageCenter.saveTransactionMsg(transactionMsgFmDto);

                    return getSuccessResult(version, ctx, transactionId);
                default:
                    return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }

        }catch (FmException e){
            log.error(OPERATION_NAME + " FmException info = " + e.getMessage());
            if (-1 != e.getCode()) {
                return getErrorResult(ctx, version, OPERATION_NAME, e.getCode());
            }else{
                return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(OPERATION_NAME + " error info = " + e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME);
        } finally {
            log.info("机器结束 transactionMsg 耗时：" + (System.currentTimeMillis() - t1) );
        }
    }

    private byte[] getSuccessResult(String version, ChannelHandlerContext ctx, String transactionId) throws Exception {
        /**
         typedef  struct{
             unsigned char length[4];				 //2个字节
             unsigned char type;				 	     //0xBA
             unsigned char operateID[2];
             unsigned char content;				     //加密内容: result(长度为2 1 成功) + version + transactionId（36）
             result(长度为2 不为1,操作失败具体原因看 FMResultEnum) + 版本内容(3)
             unsigned char check;				     //校验位
             unsigned char tail;					     //0xD0
         }__attribute__((packed))TransactionsMsgResult, *TransactionsMsgResult;
         */
        String responseData = FMResultEnum.SUCCESS.getSuccessCode()
                + version
                + transactionId;
        String tempKey = transactionMsgPortocol.tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("transactionMsg 协议：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }
}
