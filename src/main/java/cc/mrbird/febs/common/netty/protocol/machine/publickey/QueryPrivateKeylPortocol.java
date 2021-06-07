package cc.mrbird.febs.common.netty.protocol.machine.publickey;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.service.IPublicKeyService;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@NoArgsConstructor
@Component
public class QueryPrivateKeylPortocol extends MachineToServiceProtocol {

    @Autowired
    IPublicKeyService publicKeyService;

    public static final byte PROTOCOL_TYPE = (byte) 0xB8;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    private static final String OPERATION_NAME = "QueryPrivateKeylPortocol";

    public static QueryPrivateKeylPortocol queryPrivateKeylPortocol;

    @PostConstruct
    public void init(){
        this.queryPrivateKeylPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return queryPrivateKeylPortocol;
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
            typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[2];				//
                unsigned char type;					//0xB8
                unsigned char acnum[6];             //机器表头号
                unsigned char version[3];           //版本号
                unsigned char content[?];			//加密后内容: frankMachineId
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))privateKey, *privateKey;
         */
            //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
            String key = ctx.channel().id().toString() + "_" + OPERATION_NAME;
            if (queryPrivateKeylPortocol.redisService.hasKey(key)) {
                return getOverTimeResult(version, ctx, key, FMResultEnum.Overtime.getCode());
            } else {
                log.info("channelId={}的操作记录放入redis", key);
                queryPrivateKeylPortocol.redisService.set(key, "wait", WAIT_TIME);
            }
            log.info("机器开始 ForeseensCancel");

            int pos = TYPE_LEN;

            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //版本号
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;

            switch (version) {
                case FebsConstant.FmVersion1:
                    String frankMachineId = getDecryptContent(bytes, ctx, pos, REQ_ACNUM_LEN).trim();
                    return getSuccessResult(version, ctx, frankMachineId);
                default:
                    return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }

        } catch (Exception e) {
            log.error(OPERATION_NAME + "error info = " + e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME);
        } finally {
            log.info("机器结束 ForeseensCancelPortocol");
        }
    }

    private byte[] getSuccessResult(String version, ChannelHandlerContext ctx, String frankMachineId) throws Exception {
        /**
         typedef  struct{
             unsigned char length[2];				     //2个字节
             unsigned char head;				 	     //0xB8
             unsigned char content;				     //加密内容: result(长度为2 0 失败 1 成功) + version + privateKey的加密内容
             result(长度为2 不为1,操作失败具体原因看 FMResultEnum) + 版本内容(3)
             unsigned char check;				     //校验位
             unsigned char tail;					     //0xD0
         }__attribute__((packed))privateKeyRes, *privateKeyRes;
         */
        PublicKey dbPublicKey = queryPrivateKeylPortocol.publicKeyService.findByFrankMachineId(frankMachineId);
        String privateKey = dbPublicKey.getPrivateKey();

        String responseData = FMResultEnum.SUCCESS.getSuccessCode() + version + privateKey;
        String tempKey = queryPrivateKeylPortocol.tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("CancelJob 协议：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }
}
