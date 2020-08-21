package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.netty.SocketData;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 协议处理
 */
@Slf4j
@Component
public class ProtocolService {

    @Autowired
    HeartPortocol heartPortocol;

    @Autowired
    QueryProtocol queryProtocol;

    @Autowired
    ChargeResProtocol chargeResProtocol;

    @Autowired
    QueryIDPortocol queryIDPortocol;

    @Autowired
    QueryTemKeyPortocol queryTemKeyPortocol;

    //出问题了返回该结果
    private byte[] emptyResBytes = new byte[]{(byte) 0xA0, (byte) 0xFF, (byte) 0xD0};

    @Autowired
    private ApplicationContext applicationContext;

    public BaseProtocol parseAndResponse(SocketData msg, ChannelHandlerContext ctx) {
        if (msg == null) {
            log.error("socketData为null，不可用");
            return null;
        }
        BaseProtocol protocol = null;
        try {
            log.error("客户端【" + ctx.channel().id() + "】发送数据给客户端");
            wrieteToCustomer(ctx, parseContentAndRspone(msg.getContent(), ctx));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
        return protocol;
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
     * 解析数据内容
     *
     * @param data
     * @return
     */
    public byte[] parseContentAndRspone(byte[] data, ChannelHandlerContext ctx) {
        //验证校验位
        if (BaseTypeUtils.checkChkSum(data, data.length - 2)) {
            //解析类型
            byte protocolType = BaseProtocol.parseType(data);

            BaseProtocol baseProtocol = null;
            switch (protocolType) {
                case HeartPortocol.PROTOCOL_TYPE:
                    baseProtocol = heartPortocol;
                    break;
                case QueryIDPortocol.PROTOCOL_TYPE:
                    baseProtocol = queryIDPortocol;
                    break;
                case QueryTemKeyPortocol.PROTOCOL_TYPE:
                    baseProtocol = queryTemKeyPortocol;
                    break;
                case QueryProtocol.PROTOCOL_TYPE:
                    baseProtocol = queryProtocol;
                    break;
                case ChargeResProtocol.PROTOCOL_TYPE:
                    baseProtocol = chargeResProtocol;
                    break;
                default:
                    return emptyResBytes;
            }

            try {
                return baseProtocol.parseContentAndRspone(data, ctx);
            } catch (Exception e) {
                return emptyResBytes;
            }
        } else {
            log.error("校验位验证错误");
            return emptyResBytes;
        }
    }
}
