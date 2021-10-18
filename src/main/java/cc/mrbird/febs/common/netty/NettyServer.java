package cc.mrbird.febs.common.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * @author Gjing
 * <p>
 * 服务启动监听器
 **/

@Component
public class NettyServer {
    Logger logger = LoggerFactory.getLogger(NettyServer.class);
    //证书信息
    private final static String serverCrt = "/static/keys/server.crt";
    private final static String serverKey = "/static/keys/pkcs8_server.key";
    private final static String caCrt = "/static/keys/ca.crt";
    private final static String keyPassword = "123456";

   /* @Autowired
    NettyServerHandler nettyServerHandler;*/

    private Channel channel;

    /**
     * NioEventLoop并不是一个纯粹的I/O线程，它除了负责I/O的读写之外
     * 创建了两个NioEventLoopGroup，
     * 它们实际是两个独立的Reactor线程池。
     * 一个用于接收客户端的TCP连接，
     * 另一个用于处理I/O相关的读写操作，或者执行系统Task、定时任务Task等。
     */
    //new 一个主线程组
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    //new 一个工作线程组
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start(String hostname, int port, boolean isSsl) {
        ChannelFuture f = null;

        InetSocketAddress socketAddress = new InetSocketAddress(hostname, port);

        ServerBootstrap bootstrap = new ServerBootstrap();
        ServerBootstrap serverBootstrap = bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 添加SSL安装验证
                        if (isSsl) {
                            SslContext sslCtx = buildSslContext(ClientAuth.REQUIRE);
                            if (sslCtx != null) {
                                socketChannel.pipeline().addLast(sslCtx.newHandler(socketChannel.alloc()));
                            }
                        }
                        //如果客户端60秒没有任何请求,就关闭客户端连接（很重要）
                        socketChannel.pipeline().addLast("readtime", new ReadTimeoutHandler(20));
                        // 解决粘包问题 通过解析不定长的协议
                        socketChannel.pipeline().addLast("decoder", new MyDecoder());

//                        socketChannel.pipeline().addLast(nettyServerHandler);
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                })
                .localAddress(socketAddress)
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, 4 * 1024)
                //设置接受的缓存大小
                .option(ChannelOption.SO_RCVBUF, 128 * 1024)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //socketchannel的设置,关闭延迟发送
                .childOption(ChannelOption.TCP_NODELAY, true);

        //绑定端口,开始接收进来的连接
        try {
            ChannelFuture future = bootstrap.bind(socketAddress).sync();
            logger.info("服务器启动开始监听端口: {}", socketAddress.getPort());
            logger.info(isSsl ? "启动SSL" : "关闭SSL");

            //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
            future.channel().closeFuture().sync();
            channel = future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭主线程组
            bossGroup.shutdownGracefully();
            //关闭工作线程组
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 停止服务
     */
    public void destroy() {
        logger.info("Shutdown Netty Server...");
        if (channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        logger.info("Shutdown Netty Server Success!");
    }

    public SslContext buildSslContext(ClientAuth clientAuth) {
        InputStream certInput = null;
        InputStream priKeyInput = null;
        InputStream caInput = null;
        try {
            certInput = NettyServer.class.getResourceAsStream(serverCrt);
            priKeyInput = NettyServer.class.getResourceAsStream(serverKey);
            caInput = NettyServer.class.getResourceAsStream(caCrt);

            return SslContextBuilder.forServer(certInput, priKeyInput)
                    .clientAuth(clientAuth)
                    .trustManager(caInput).build();
        } catch (Throwable e) {
            logger.error("HidsSslContextBuilder", e);
        } finally {
            IOUtils.closeQuietly(certInput);
            IOUtils.closeQuietly(priKeyInput);
            IOUtils.closeQuietly(caInput);
        }
        return null;
    }

}