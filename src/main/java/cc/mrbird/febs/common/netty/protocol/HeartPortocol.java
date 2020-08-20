package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class HeartPortocol extends BaseProtocol {
    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    public static final ConcurrentHashMap<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

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

        if (acnum.trim().length() != 6){
            throw new Exception("表头号不正确");
        }

        log.info("心跳包中表头号：" + acnum + "  连接通道数量 = " + CHANNEL_MAP.size());

        //添加到全局的连接通道
        if (CHANNEL_MAP.containsKey(acnum)) {
            //这里有个问题:如果有人一直仿造心跳包，会挤掉真正的心跳包通讯，所以要注释掉
            /*if (ctx != CHANNEL_MAP.get(acnum)) {
                CHANNEL_MAP.get(acnum).close();
                CHANNEL_MAP.put(acnum, ctx);
            }*/
//            log.info("客户端【" + acnum + "】已经是连接状态，连接通道数量: " + CHANNEL_MAP.size() + " ctx = " + ctx.toString());
        } else {
            //保存连接
            CHANNEL_MAP.put(acnum, ctx);
//            log.info("客户端【" + acnum + "】连接netty服务器" + " ctx = " + ctx.toString());
//            log.info("连接通道数量: " + CHANNEL_MAP.size());
        }

        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }


    /**
     * 获取在线的机器列表
     *
     * @return
     */
    public static List<String> getAcnumList() {
        List<String> acnumList = new ArrayList<>();
        for (String key : CHANNEL_MAP.keySet()) {
            acnumList.add(key);
        }
        return acnumList;
    }

}
