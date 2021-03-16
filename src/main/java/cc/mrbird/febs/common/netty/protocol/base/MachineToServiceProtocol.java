package cc.mrbird.febs.common.netty.protocol.base;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>长度 + 类型 + 数据 + 检验位 + 结尾</b>
 */
@Slf4j
public abstract class MachineToServiceProtocol extends BaseProtocol {


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
     * 解析并返回结果流
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
}
