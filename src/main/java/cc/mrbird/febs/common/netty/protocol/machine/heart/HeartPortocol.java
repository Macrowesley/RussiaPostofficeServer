package cc.mrbird.febs.common.netty.protocol.machine.heart;

import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeartPortocol extends MachineToServiceProtocol {

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //返回数据长度
    private static final int RES_DATA_LEN = 1;

    public static final byte PROTOCOL_TYPE = (byte) 0xA0;

    /**
     * 获取协议类型
     * A0 心跳包
     */
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    /**
     * 解析并返回结果流
     *
     * @param bytes
     */
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        int pos = TYPE_LEN;

        //解析表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        if (acnum.trim().length() != 6) {
            throw new Exception("表头号不正确");
        }

        //todo 临时代码，要删掉
        if (!ChannelMapperUtils.containsKey(acnum)) {
            ChannelMapperUtils.addChannel(acnum, ctx);
        }
        log.info("心跳包中表头号：" + acnum);

        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }

}
