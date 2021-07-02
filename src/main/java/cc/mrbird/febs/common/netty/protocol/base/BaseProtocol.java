package cc.mrbird.febs.common.netty.protocol.base;

import cc.mrbird.febs.common.netty.protocol.dto.TransactionFMDTO;
import cc.mrbird.febs.common.netty.protocol.kit.TempKeyUtils;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.rcs.api.ServiceManageCenter;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;

@Slf4j
@NoArgsConstructor
public abstract class BaseProtocol {

    //请求长度：记录整条数据长度数值的长度
    public static final int REQUEST_LENGTH_LEN = 2;

    //响应长度：记录整条数据长度数值的长度
    public static final int RESPONSE_LENGTH_LEN = 2;

    //操作id 长度
    public static final int OPERATEID_LEN = 2;

    //协议的数据类型长度
    public static final int TYPE_LEN = 1;
    //校验位长度
    protected static final int CHECK_LEN = 1;
    //结尾长度
    protected static final int END_LEN = 1;

    //总长度是否包含第一个4字节的长度 ： 否
    protected static final boolean isContainFirstLen = false;

    //版本内容 3
    protected static final int VERSION_LEN = 3;

    @Autowired
    public TempKeyUtils tempKeyUtils;

    @Autowired
    public RedisService redisService;

    @Autowired
    public ServiceManageCenter serviceManageCenter;

    public byte[] operateIdArr;

    /**
     * 获取实际操作的类
     * @return
     */
    public abstract BaseProtocol getOperator();

    public int getBeginPos(){
        return TYPE_LEN + OPERATEID_LEN;
    }
    /**
     * 获取返回的协议内容长度 获取操作人员
     * isContainFirstLen : 总长度是否包含第一个4字节的长度
     *
     * @return
     */
    public int getResponseProtocolLen(byte[] data) {
        if (isContainFirstLen) {
            return RESPONSE_LENGTH_LEN + TYPE_LEN + OPERATEID_LEN + data.length + CHECK_LEN + END_LEN;
        } else {
            return TYPE_LEN + OPERATEID_LEN + data.length + CHECK_LEN + END_LEN;
        }
    }

    /**
     * 拼接发送给客户端的数据
     * length + type + data + checkSum + end
     * @return
     */
    public byte[] getWriteContent(byte[] data, byte type) {
//        log.info("拼接发送给客户端的数据");
        int protocolLen = getResponseProtocolLen(data);
        byte[] length = BaseTypeUtils.int2ByteArrayCons(protocolLen);
        byte[] typeData = new byte[]{type};
        byte[] checkSume = BaseTypeUtils.makeCheckSum(BaseTypeUtils.byteMerger(typeData, data));
        byte[] end = {(byte) 0xD0};

        int totalLen = 0;
        if (isContainFirstLen) {
            totalLen = protocolLen;
        } else {
            totalLen = RESPONSE_LENGTH_LEN + protocolLen;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(totalLen);
        baos.write(length, 0, RESPONSE_LENGTH_LEN);
        baos.write(typeData, 0, TYPE_LEN);
        baos.write(getOperateIdArr(), 0, OPERATEID_LEN);
        baos.write(data, 0, data.length);
        baos.write(checkSume, 0, CHECK_LEN);
        baos.write(end, 0, END_LEN);

        if (type != (byte) 0xa0) {
            log.info("最后发送给客户端的数据：" + BaseTypeUtils.bytesToHexString(baos.toByteArray()));
        }
        return baos.toByteArray();
    }

    /**
     * 解析出协议类型
     *
     * @param data
     * @return
     */
    public static byte parseType(byte[] data) {
        return data[0];
    }

    /**
     * 服务端给客户端发送消息
     */
    public void wrieteToCustomer(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
        if (ctx == null) {
            log.info("通道不存在");
            return;
        }

        if (bytes == null) {
            log.info("服务端响应空的消息");
            return;
        }

        ByteBuf buf = Unpooled.buffer(bytes.length);
        buf.writeBytes(bytes);
        ctx.write(buf);
        ctx.flush();
    }

    /**
     * 给加密内容解密
     * @param bytes
     * @param ctx
     * @param pos
     * @param REQ_ACNUM_LEN
     * @return
     * @throws Exception
     */
    public String getDecryptContent(byte[] bytes, ChannelHandlerContext ctx, int pos, int REQ_ACNUM_LEN) throws Exception {
        String enctryptContent = BaseTypeUtils.byteToString(bytes, pos, bytes.length - TYPE_LEN - OPERATEID_LEN - REQ_ACNUM_LEN - VERSION_LEN - CHECK_LEN - END_LEN, BaseTypeUtils.UTF8);
//        log.info("baseProtocol = " + getOperator().toString());
        //获取临时密钥
        String tempKey = getOperator().tempKeyUtils.getTempKey(ctx);
        //todo 测试
//        tempKey = "2dc1f4d99e7fcadc";
        //解密后内容
        return AESUtils.decrypt(enctryptContent, tempKey);
    }

    /**
     * 解析加密内容成对象
     * @param bytes
     * @param ctx
     * @param pos
     * @param REQ_ACNUM_LEN
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T parseEnctryptToObject(byte[] bytes, ChannelHandlerContext ctx, int pos, int REQ_ACNUM_LEN, Class<T> clazz) throws Exception {
        String decryptContent = getDecryptContent(bytes, ctx, pos, REQ_ACNUM_LEN);
        log.info("解析得到的内容：decryptContent={}",decryptContent);
        T bean = JSON.parseObject(decryptContent, clazz);
        return bean;
    }

    public byte[] getOperateIdArr() {
        if (operateIdArr == null){
            return new byte[]{0x00,0x00};
        }
        return operateIdArr;
    }

    public void setOperateIdArr(byte[] operateIdArr) {
        this.operateIdArr = operateIdArr;
    }
}
