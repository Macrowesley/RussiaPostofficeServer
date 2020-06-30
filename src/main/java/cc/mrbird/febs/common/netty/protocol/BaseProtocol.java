package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

/**
 * <b>长度 + 类型 + 数据 + 检验位 + 结尾</b>
 */
@Slf4j
public abstract class BaseProtocol {

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

    /**
     * 获取协议类型
     * A0 心跳包
     * A1 查询是否有数据包
     * A2 返回注资结果
     *
     * @return
     */
    public abstract byte getProtocolType();

    /**
     * 获取发送过来的协议数据部分的长度
     * @return
     */
    public abstract int getRequestDataLen();

    /**
     * 获取返回的协议数据部分的长度
     * @return
     */
    public abstract int getResponsetDataLen();
    /**
     * 解析并返回结果流
     *
     * @param bytes
     * @return
     */
    public abstract byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception;

    /**
     * 获取返回的协议内容长度
     * isContainFirstLen : 总长度是否包含第一个4字节的长度
     *
     * @return
     */
    public int getResponseProtocolLen() {
        if (isContainFirstLen) {
            return LENGTH_LEN + TYPE_LEN + getResponsetDataLen() + CHECK_LEN + END_LEN;
        } else {
            return TYPE_LEN + getResponsetDataLen() + CHECK_LEN + END_LEN;
        }
    }

    /**
     * 拼接发送给客户端的数据
     * length + type + data + checkSum + end
     * @return
     */
    public byte[] getWriteContent(byte[] data) {
//        log.info("拼接发送给客户端的数据");
        byte[] length = BaseTypeUtils.int2ByteArrayCons(getResponseProtocolLen());
        byte[] type = new byte[]{getProtocolType()};
        byte[] checkSume = BaseTypeUtils.makeCheckSum(BaseTypeUtils.byteMerger(type, data));
        byte[] end = {(byte) 0xD0};

        int totalLen = 0;
        if (isContainFirstLen) {
            totalLen = getResponseProtocolLen();
        } else {
            totalLen = LENGTH_LEN + getResponseProtocolLen();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(totalLen);
        baos.write(length, 0, LENGTH_LEN);
        baos.write(type, 0, TYPE_LEN);
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

}
