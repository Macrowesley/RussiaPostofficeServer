package cc.mrbird.febs.common.netty;


import cc.mrbird.febs.common.netty.protocol.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.HeartPortocol;
import cc.mrbird.febs.common.netty.protocol.ProtocolService;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty服务端处理器
 **/

@Sharable
@Service
public class NettyServerHandler extends SimpleChannelInboundHandler<SocketData> {
    Logger log = LoggerFactory.getLogger(NettyServerHandler.class);

    @Autowired
    ProtocolService protocolService;
    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();


    /**
     * @param ctx
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();

        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();

        //如果map中不包含此连接，就保存连接
        if (CHANNEL_MAP.containsKey(channelId)) {
            log.info("客户端【" + channelId + "】是连接状态，连接通道数量: " + CHANNEL_MAP.size() + " insocket="+insocket.toString());
        } else {
            //保存连接
            CHANNEL_MAP.put(channelId, ctx);

            log.info("客户端【" + channelId + "】连接netty服务器[IP:" + clientIp + "--->PORT:" + clientPort + "]" + " insocket="+insocket.toString());
            log.info("连接通道数量: " + CHANNEL_MAP.size());
        }
    }

    /**
     * 有客户端终止连接服务器会触发此函数
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.error("客户端【" + ctx.channel().id() + "】终止连接服务器");
        removeCache(ctx);
    }

    /**
     * 删除缓存
     * @param ctx
     */
    private void removeCache(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();

        ChannelId channelId = ctx.channel().id();

        //包含此客户端才去删除
        if (CHANNEL_MAP.containsKey(channelId)) {

            ChannelHandlerContext temp = CHANNEL_MAP.get(channelId);
            if (HeartPortocol.CHANNEL_MAP.containsValue(temp)) {
                for (Map.Entry<String, ChannelHandlerContext> entry : HeartPortocol.CHANNEL_MAP.entrySet()) {
                    if (entry.getValue() == temp) {
                        HeartPortocol.CHANNEL_MAP.remove(entry.getKey());
                        log.info("删除心跳包中保存的连接通道1 ");
                    }
                }
                log.info("删除心跳包中保存的连接通道2");
            }

            //删除连接
            CHANNEL_MAP.remove(channelId);
            log.info("删除handler中保存的连接通道");


            System.out.println();
            log.info("客户端【" + channelId + "】退出netty服务器[IP:" + clientIp + "--->PORT:" + insocket.getPort() + "]");
            log.info("连接通道数量: " + CHANNEL_MAP.size());
        }
    }

    /**
     * 有客户端发消息会触发此函数
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocketData msg) throws Exception {
        protocolService.parseAndResponse(msg, ctx);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        String socketString = ctx.channel().remoteAddress().toString();

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("Client: " + socketString + " READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("Client: " + socketString + " WRITER_IDLE 写超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("Client: " + socketString + " ALL_IDLE 总超时");
                ctx.disconnect();
            }
        }
    }

    /**
     * 发生异常会触发此函数
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端发生异常，断开连接");
        removeCache(ctx);
        log.info(ctx.channel().id() + " 发生了错误,此连接被关闭" + "此时连通数量: " + CHANNEL_MAP.size());
        cause.printStackTrace();
        ctx.close();
    }

    public static ConcurrentHashMap<ChannelId, ChannelHandlerContext> getChannelMap() {
        return CHANNEL_MAP;
    }
}
