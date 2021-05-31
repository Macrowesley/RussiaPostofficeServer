package cc.mrbird.febs.common.netty.protocol.base;

import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.rcs.api.ServiceManageCenter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.omg.IOP.Codec;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <b>长度 + 类型 + 数据 + 检验位 + 结尾</b>
 */
@Slf4j
public abstract class MachineToServiceProtocol extends BaseProtocol {
    @Autowired
    public RedisService redisService;

    @Autowired
    public ServiceManageCenter serviceManageCenter;

    //预计一次操作的最长等待时间
    public static final long WAIT_TIME = 60L;

    /**
     * 获取协议类型
     *
     * @return
     */
    public abstract byte getProtocolType();

    /**
     * 解析并返回结果流 注意判断通道是否在缓存中
     *
     * @param bytes
     * @return
     */
    public abstract byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception;

    /**
     * 拼接发送给客户端的数据
     * length + type + data + checkSum + end
     *
     * @param data
     * @param type
     * @return
     */
    public byte[] getWriteContent(byte[] data) {
        return super.getWriteContent(data, getProtocolType());
    }

    public byte[] getErrorResult(ChannelHandlerContext ctx, String version, String operationName) throws Exception {
        log.error("返回错误结果");
        try {
            return getErrorResult(ctx, version,operationName, FMResultEnum.DefaultError.getCode());
        }catch (Exception e){
            log.error(e.getMessage());
            return new byte[]{0x00, 0x01};
        }

    }
    public byte[] getErrorResult(ChannelHandlerContext ctx, String version, String operationName, int resCode) throws Exception {
        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char head;				 	 //
         unsigned char content[?];				 //加密内容:   result(2位 01,操作成功，则后面再添加几个参数，可以作为验证) + 版本内容(3) + event(1) + status(1)
                                                             result(2位 不为01,操作失败 FMResultEnum为准
                                                                         0 其他异常导致的失败
                                                                         2 请求太频繁，1分钟内请勿重复请求
                                                                         3 TransactionError异常，需要继续执行transaction
                                                                         4 没有闭环，请等待
                                                                         5 版本信息不对
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))status, *status;
         */
        //删除redis缓存
        redisService.del(ctx.channel().id().toString());

        //返回内容的原始数据
        String responseData = String.format("%02d", resCode) + version;

        //返回内容的加密数据
        //获取临时密钥
        String tempKey = tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.error("操作出错了  操作名："+operationName+"：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }

    /**
     * 指定时间内多次请求返回结果
     * @param version
     * @param ctx
     * @param resCode
     * @return
     * @throws Exception
     */
    public byte[] getOverTimeResult(String version, ChannelHandlerContext ctx, String key, int resCode) throws Exception {
        log.error("操作{} 在指定时间内多次请求返回结果", key);
        String responseData = String.format("%02d", resCode) + version ;
        //返回内容的加密数据
        //获取临时密钥
        String tempKey = tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }

}
