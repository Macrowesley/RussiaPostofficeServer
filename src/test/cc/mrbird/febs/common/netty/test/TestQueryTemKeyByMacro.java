package netty.test;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.dto.CheckServiceDTO;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionMsgFMDTO;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * 任务：
 * 1. 读懂代码
 * 2. 完善transactionMsg//打印过程为transactionMsg，最后执行transaction
 * 3. 测试大量用户访问的打印任务 foreseen transactionMsg transaction
 * 4. 测试大量用户的 CheckServicePortocol
 * 5. 测试大量用户的 heartPortocol
 * 6. 。。。其他接口都能测试，碰到问题先记录，能自己解决最好，不能解决等我回来，测试在你自己的分支
 * 【PS】 大量用户 = 1000，先用数量为1确保流程正常
 */
@Slf4j
@Component
@NoArgsConstructor
public class TestQueryTemKeyByMacro implements Runnable{
    private int pos = 0;
    //请求长度：记录整条数据长度数值的长度
    public static final int REQUEST_LENGTH_LEN = 2;

    //响应长度：记录整条数据长度数值的长度
    public static final int RESPONSE_LENGTH_LEN = 2;

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

    public TestQueryTemKeyByMacro(int pos) {
        this.pos = pos;
        log.info("pos = " + pos + "当前线程" + Thread.currentThread().getName());
    }

    @Override
    public void run() {
        //String acnum = "CPU" + String.format("%03d", 1);
        try {
            // 首先，netty通过ServerBootstrap启动服务端
            Bootstrap client = new Bootstrap();

            //第1步 定义线程组，处理读写和链接事件，没有了accept事件
            EventLoopGroup group = new NioEventLoopGroup();
            client.group(group);

            //第2步 绑定客户端通道
            client.channel(NioSocketChannel.class);

            //第3步 给NIoSocketChannel初始化handler， 处理读写事件
            client.handler(new ChannelInitializer<NioSocketChannel>() {  //通道是NioSocketChannel
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
//                ch.pipeline().addLast(sslCtx.newHandler(ch.alloc()));
                    //字符串编码器，一定要加在SimpleClientHandler 的上面
//                ch.pipeline().addLast(new StringEncoder());
                    //ch.pipeline().addLast(new TestDecode());
//                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(
//                        Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                    //找到他的管道 增加他的handler
                    ch.pipeline().addLast(new SimpleClientHandler());
                }
            });

            //连接服务器
            ChannelFuture future = client.connect("119.37.199.25", 12801).sync();

            long millis = 3000;

            String foreseenId = AESUtils.createUUID();
            String transactionId = AESUtils.createUUID();

            //发送数据给服务器
            testQueryTemKey(future);


            Thread.sleep(3000);
            //当通道关闭了，就继续往下走
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("客户端结束");
    }

    private static void testQueryTemKey(ChannelFuture future) {
        /*
         * 机器请求临时秘钥
         * Typedef struct{
            unsigned char head;                 //0xAA
            unsigned char length[4];
            unsigned char type;                 //0xA4，用来区分协议
            unsigned char operateID[2];
            unsigned char version[3];           //版本内容 001
            unsigned char acnum[6];             //机器的表头号
            unsigned char check;                //校验位
            unsigned char tail;                 //0xD0
        }__attribute__((packed))QueryInfo,*pQueryInfo;
         */
        log.info("获取临时密钥开始");

        int protocolLen = TYPE_LEN + 2 + 3 + 6 + CHECK_LEN + END_LEN;
        int totalLen = 3 + protocolLen;

        byte[] head = new byte[]{(byte) 0xAA};
        byte[] length = BaseTypeUtils.int2ByteArrayCons(protocolLen);
        byte[] typeData = new byte[]{(byte) 0xA4};
        byte[] operateIdData = new byte[]{(byte) 0xFF, (byte) 0XFF};
        byte[] acnumData = BaseTypeUtils.stringToByte("CPU123", BaseTypeUtils.UTF8);
        byte[] versionData = BaseTypeUtils.stringToByte("001", BaseTypeUtils.UTF8);
        byte[] finalData = BaseTypeUtils.byteMerger(typeData, operateIdData);
        finalData = BaseTypeUtils.byteMerger(finalData, acnumData);
        finalData = BaseTypeUtils.byteMerger(finalData, versionData);

        byte[] checkSume = BaseTypeUtils.makeCheckSum(finalData);
        byte[] end = {(byte) 0xD0};

        ByteArrayOutputStream baos = new ByteArrayOutputStream(totalLen);
        baos.write(head, 0, 1);
        baos.write(length, 0, RESPONSE_LENGTH_LEN);
        baos.write(typeData, 0, TYPE_LEN);
        baos.write(operateIdData, 0, 2);
        baos.write(versionData, 0, 3);
        baos.write(acnumData, 0, 6);
        baos.write(checkSume, 0, CHECK_LEN);
        baos.write(end, 0, END_LEN);

        ByteBuf buf = Unpooled.buffer(baos.toByteArray().length);
        buf.writeBytes(baos.toByteArray());
        future.channel().writeAndFlush(buf);
        log.info("获取临时密钥结束");
    }

}
