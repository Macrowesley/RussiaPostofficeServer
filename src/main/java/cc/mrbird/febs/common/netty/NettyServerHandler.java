package cc.mrbird.febs.common.netty;


import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
import cc.mrbird.febs.common.netty.protocol.machine.heart.HeartPortocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty服务端处理器
 **/
@Slf4j
//@Sharable
@Service
public class NettyServerHandler extends SimpleChannelInboundHandler<SocketData> {
//    Logger log = LoggerFactory.getLogger(NettyServerHandler.class);

    public static NettyServerHandler nettyServerHandler;

    @Autowired
    ProtocolService protocolService;

    @Autowired
    HeartPortocol heartPortocol;

    @Autowired
    ChannelMapperManager channelMapperManager;

    @Autowired
    @Qualifier(value = FebsConstant.NETTY_ASYNC_POOL)
    ThreadPoolTaskExecutor taskExecutor;

    public NettyServerHandler() {
    }

    @PostConstruct
    public void init(){
        log.info("NettyServerHandler 初始化 对象是：" + this);
        nettyServerHandler = this;
    }


    /**
     * @param ctx
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("channelActive NettyServerHandler 对象是：" + this);

        /*ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                        if(future.isSuccess()){
                            System.out.println("握手成功");

                            SSLSession ss =  ctx.pipeline().get(SslHandler.class).engine().getSession();
                            System.out.println("cipherSuite:"+ss.getCipherSuite());
                            X509Certificate cert = ss.getPeerCertificateChain()[0];
                            String info = null;
                            // 获得证书版本
                            info = String.valueOf(cert.getVersion());
                            System.out.println("证书版本:" + info);
                            // 获得证书序列号
                            info = cert.getSerialNumber().toString(16);
                            System.out.println("证书序列号:" + info);
                            // 获得证书有效期
                            Date beforedate = cert.getNotBefore();
                            info = new SimpleDateFormat("yyyy/MM/dd").format(beforedate);
                            System.out.println("证书生效日期:" + info);
                            Date afterdate = (Date) cert.getNotAfter();
                            info = new SimpleDateFormat("yyyy/MM/dd").format(afterdate);
                            System.out.println("证书失效日期:" + info);
                            // 获得证书主体信息
                            info = cert.getSubjectDN().getName();
                            System.out.println("证书拥有者:" + info);
                            // 获得证书颁发者信息
                            info = cert.getIssuerDN().getName();
                            System.out.println("证书颁发者:" + info);
                            // 获得证书签名算法名称
                            info = cert.getSigAlgName();
                            System.out.println("证书签名算法:" + info);

                        }else{
                            System.out.println("握手失败");
                        }
                    }
                });
*/

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();

        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();

        //如果map中不包含此连接，就保存连接
        if (ChannelMapperManager.All_CHANNEL_MAP.containsKey(channelId)) {
            log.info("客户端【" + channelId + "】是连接状态，连接通道数量: " + ChannelMapperManager.All_CHANNEL_MAP.size() + " insocket="+insocket.toString()  + " 有效连接数量：" + nettyServerHandler.channelMapperManager.getChannleSize());
        } else {
            //保存连接
            ChannelMapperManager.All_CHANNEL_MAP.put(channelId, ctx);

            log.info("客户端【" + channelId + "】连接netty服务器[IP:" + clientIp + "--->PORT:" + clientPort + "]" + " insocket="+insocket.toString());
            log.info("连接通道数量: " + ChannelMapperManager.All_CHANNEL_MAP.size() + " 有效连接数量：" + nettyServerHandler.channelMapperManager.getChannleSize() );
        }
    }

    /**
     * 有客户端终止连接服务器会触发此函数
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.error("客户端【" + ctx.channel().id() + "】终止连接服务器");
        nettyServerHandler.channelMapperManager.removeCache(ctx);
    }



    /**
     * 有客户端发消息会触发此函数
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, SocketData msg) throws Exception {
        if (msg == null) {
            log.error("socketData为null，不可用");
            return;
        }

        byte[] data = msg.getContent();
        try {
            //验证校验位 测试情况除外
            if (!BaseTypeUtils.checkChkSum(data, data.length - 2) && !FebsConstant.IS_TEST_NETTY) {
                log.error("校验位验证错误");
                nettyServerHandler.protocolService.wrieteToCustomer(ctx, nettyServerHandler.protocolService.emptyResBytes);
            }

            //解析类型
            byte protocolType = MachineToServiceProtocol.parseType(data);

            if (protocolType == HeartPortocol.PROTOCOL_TYPE) {
                byte[] operateIdArr = new byte[]{data[1], data[2]};
                nettyServerHandler.heartPortocol.setOperateIdArr(operateIdArr);
                nettyServerHandler.protocolService.wrieteToCustomer(ctx, nettyServerHandler.heartPortocol.parseContentAndRspone(data, ctx));
            } else {
                nettyServerHandler.protocolService.parseContentAndWrite(data, protocolType, ctx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
            data = null;
        }

        /*nettyServerHandler.taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                nettyServerHandler.protocolService.parseAndResponse(msg, ctx);
            }
        });*/
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        String socketString = ctx.channel().remoteAddress().toString();

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("Client: " + socketString + " READER_IDLE 读超时");
                nettyServerHandler.channelMapperManager.removeCache(ctx);
                ctx.disconnect().sync();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("Client: " + socketString + " WRITER_IDLE 写超时");
                nettyServerHandler.channelMapperManager.removeCache(ctx);
                ctx.disconnect().sync();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("Client: " + socketString + " ALL_IDLE 总超时");
                nettyServerHandler.channelMapperManager.removeCache(ctx);
                ctx.disconnect().sync();
            }
        }
    }

    /**
     * 发生异常会触发此函数
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        nettyServerHandler.channelMapperManager.removeCache(ctx);
        log.error("客户端" + ctx.channel().id() + " 发生异常，此连接被关闭， " +
                "此时连通数量: " + ChannelMapperManager.All_CHANNEL_MAP.size()  + "" +
                " 有效连接数量：" + nettyServerHandler.channelMapperManager.getChannleSize() + "" +
                " 原因：" + cause.toString());
//        cause.printStackTrace();
        ctx.close();
    }

    public static ConcurrentHashMap<ChannelId, ChannelHandlerContext> getChannelMap() {
        return ChannelMapperManager.All_CHANNEL_MAP;
    }
}
