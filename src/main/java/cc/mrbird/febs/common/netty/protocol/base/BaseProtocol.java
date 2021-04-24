package cc.mrbird.febs.common.netty.protocol.base;

import cc.mrbird.febs.common.netty.protocol.kit.TempKeyUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;

@Slf4j
public class BaseProtocol {

    //记录整条数据长度数值的长度
    public static final int LENGTH_LEN = 1;
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

    /**
     * 获取返回的协议内容长度
     * isContainFirstLen : 总长度是否包含第一个4字节的长度
     *
     * @return
     */
    public int getResponseProtocolLen(byte[] data) {
        if (isContainFirstLen) {
            return LENGTH_LEN + TYPE_LEN + data.length + CHECK_LEN + END_LEN;
        } else {
            return TYPE_LEN + data.length + CHECK_LEN + END_LEN;
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
            totalLen = LENGTH_LEN + protocolLen;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(totalLen);
        baos.write(length, 0, LENGTH_LEN);
        baos.write(typeData, 0, TYPE_LEN);
        baos.write(data, 0, data.length);
        baos.write(checkSume, 0, CHECK_LEN);
        baos.write(end, 0, END_LEN);
        log.info("最后发送给客户端的数据：" + BaseTypeUtils.bytesToHexString(baos.toByteArray()));
        return baos.toByteArray();
    }

    /**
     * 解析出协议类型
     *
     * @param data
     * @return
     */
    public static byte parseType(byte[] data) {
        byte t = data[0];
        return t;
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
        String enctryptContent = BaseTypeUtils.byteToString(bytes, pos, bytes.length - TYPE_LEN - REQ_ACNUM_LEN - VERSION_LEN - CHECK_LEN - END_LEN, BaseTypeUtils.UTF8);

        //获取临时密钥
        String tempKey = tempKeyUtils.getTempKey(ctx);

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

    public static void main(String[] args) throws Exception {
        String enctryptContent = "0TGmd/f+wCr7JW5Aj4EgZGFmZecRRYyztyWUn/tLZxuhDH/NLn3aC9a5u9ghNcLr9Ploz2VE4vq4FtFP0T1xlZJXjxRupTTC1mg3odRK+6pDZYzLrZMSZVQPcbqJb9v9";
        String decryptContent = AESUtils.decrypt(enctryptContent, "2c52b82ced2e2ec2");
        String json = decryptContent.substring(6 + VERSION_LEN);
        log.info("解析得到的内容：decryptContent={}",decryptContent);
        log.info("解析得到的内容：json={}",json);
        ProductCode bean = JSON.parseObject(json, ProductCode.class);
        log.info("bean = {}", bean.toString());
    }
    private static class ProductCode{
        String productCode;
        String count;
        String weight;
        String amount ;

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return productCode + " " + weight + " " + amount;
        }
    }
}
