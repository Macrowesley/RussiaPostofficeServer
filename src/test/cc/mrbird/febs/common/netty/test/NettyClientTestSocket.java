package netty.test;

import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClientTestSocket {
    int pos = 0;
    //请求长度：记录整条数据长度数值的长度
    public static final int REQUEST_LENGTH_LEN = 4;

    //响应长度：记录整条数据长度数值的长度
    public static final int RESPONSE_LENGTH_LEN = 4;

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

    static String productCode = "2100";
    static String contractCode = "00001010";
    static String frankMachineId = "PM100500";
    static String userId = "Admin";
    static String postOffice = "131000";
    static String taxVersion = "22.1.1";
    //金钱单位：分
    static String totalAmmount = "1000";
    static Integer totalCount = 10;

    public static void main(String[] args) {
        try {
            String ip = "119.37.199.25";
            ip = "192.168.2.119";
            new NettyClientTestSocket().connect(ip, 12800);
        } catch (SSLException e) {
            e.printStackTrace();
        }
    }


    private void connect(String inetHost, int inetPort) throws SSLException {

        //引入SSL安全验证
        File certChainFile = new File("C:\\Users\\Administrator\\Desktop\\SSL\\client.crt");
        File keyFile = new File("C:\\Users\\Administrator\\Desktop\\SSL\\pkcs8_client.key");
        File rootFile = new File("C:\\Users\\Administrator\\Desktop\\SSL\\ca.crt");
        SslContext sslCtx = SslContextBuilder.forClient().keyManager(certChainFile, keyFile).trustManager(rootFile).build();

        //配置客户端NIO线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ChannelInitializer<NioSocketChannel>() {  //通道是NioSocketChannel
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    log.info("添加SSL");
                    channel.pipeline().addLast(sslCtx.newHandler(channel.alloc()));
                    // 基于换行符号
                    channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    // 解码转String，注意调整自己的编码格式GBK、UTF-8
                    channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
                    // 解码转String，注意调整自己的编码格式GBK、UTF-8
                    channel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                    // 在管道中添加我们自己的接收数据实现方法
                    channel.pipeline().addLast(new SimpleClientHandler());
                }
            });
            ChannelFuture f = b.connect(inetHost, inetPort).sync();
            /**
             */
            log.info("发送数据给服务器");
            //发送数据给服务器
            for (int i = 0; i < 2; i++) {
                TimeUnit.SECONDS.sleep(1);
                testHeart(f);
            }

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static void testHeart(ChannelFuture future) {
        log.info("发送心跳包");
        /*
         * 发送心跳包
            Typedef struct{
                unsigned char head;                 //0xAA
                unsigned char length[4];
                unsigned char type;                 //0xA0
                unsigned char operateID[2];			//FF FF
                unsigned char acnum[6];             //机器的表头号
                unsigned char check;                //校验位
                unsigned char tail;                 //0xD0
            }__attribute__((packed))QueryInfo,*pQueryInfo;
        */
        int protocolLen = TYPE_LEN + 2 + 6 + CHECK_LEN + END_LEN;
        int totalLen = 3 + protocolLen;

        byte[] head = new byte[]{(byte) 0xAA};
        byte[] length = BaseTypeUtils.int2ByteArrayCons(protocolLen);
        byte[] typeData = new byte[]{(byte) 0xA0};
        byte[] operateIdData = new byte[]{(byte) 0xFF, (byte) 0XFF};
        byte[] acnumData = BaseTypeUtils.stringToByte("CPU123", BaseTypeUtils.UTF8);
        byte[] finalData = BaseTypeUtils.byteMerger(typeData, operateIdData);
        finalData = BaseTypeUtils.byteMerger(finalData, acnumData);

        byte[] checkSume = BaseTypeUtils.makeCheckSum(finalData);
        byte[] end = {(byte) 0xD0};

        ByteArrayOutputStream baos = new ByteArrayOutputStream(totalLen);
        baos.write(head, 0, 1);
        baos.write(length, 0, RESPONSE_LENGTH_LEN);
        baos.write(typeData, 0, TYPE_LEN);
        baos.write(operateIdData, 0, 2);
        baos.write(acnumData, 0, 6);
        baos.write(checkSume, 0, CHECK_LEN);
        baos.write(end, 0, END_LEN);
        System.out.println("心跳包：" + BaseTypeUtils.bytesToHexString(baos.toByteArray()));
        //心跳包：0xaa,0x0b,0x00,0xa0,0xff,0xff,0x43,0x50,0x55,0x31,0x32,0x33,0x1c,0xd0,

        ByteBuf buf = Unpooled.buffer(baos.toByteArray().length);
        buf.writeBytes(baos.toByteArray());
        future.channel().writeAndFlush(buf);

//        System.out.println("CPU666：" + BaseTypeUtils.bytesToHexString(BaseTypeUtils.stringToByte("CPU666", BaseTypeUtils.UTF8)));
    }


}
