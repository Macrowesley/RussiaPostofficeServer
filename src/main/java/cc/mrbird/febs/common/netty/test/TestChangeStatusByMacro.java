package cc.mrbird.febs.common.netty.test;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.dto.StatusFMDTO;
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

@Slf4j
@Component
@NoArgsConstructor
public class TestChangeStatusByMacro implements Runnable{
    private int pos = 0;
    public static final int REQUEST_LENGTH_LEN = 2;
    public static final int RESPONSE_LENGTH_LEN = 2;
    public static final int TYPE_LEN = 1;
    protected static final int CHECK_LEN = 1;
    protected static final int END_LEN = 1;
    protected static final boolean isContainFirstLen = false;
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

    public TestChangeStatusByMacro(int pos) {
        this.pos = pos;
        log.info("pos = "+ pos + "当前线程" + Thread.currentThread().getName());
    }


    @Override
    public void run() {
        String acnum = "CPU" + String.format("%03d", 1);
        try {
            Bootstrap client = new Bootstrap();
            EventLoopGroup group = new NioEventLoopGroup();
            client.group(group);
            client.channel(NioSocketChannel.class);
            client.handler(new ChannelInitializer<NioSocketChannel>() {  //通道是NioSocketChannel
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TestDecode());
                    ch.pipeline().addLast(new SimpleClientHandler());
                }
            });
            //连接服务器
            ChannelFuture future = client.connect("192.168.2.118", 12800).sync();
            long millis = 3000;
            int msgCount = 0;
            String foreseenId = AESUtils.createUUID();
            String transactionId = AESUtils.createUUID();


            testChangeStatus(future, acnum, millis);



            Thread.sleep(3000);
            //当通道关闭了，就继续往下走
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("客户端结束");
    }

    private static void testChangeStatus(ChannelFuture future,String acnum, long millis) throws InterruptedException {
        /*
        【使用说明】
         auth、unauth、lost、changeStatus、taxUpdate这几个操作都使用本协议
         如果是taxUpdate（更新了税率表版本）操作， event为2，其他状态操作的时候，event为1
        event
        1 STATUS
        2 RATE_TABLE_UPDATE

        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length[4];				//
            unsigned char type;					//0xB4
            unsigned char  operateID[2];
            unsigned char acnum[6];             //机器表头号
            unsigned char version[3];             //版本号
            unsigned char content[?];			//加密后内容: StatusDTO对象的json
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))status, *status;
         */
        log.info("开始ChangeStatus");
        StatusFMDTO statusFMDTO =new StatusFMDTO();
        statusFMDTO.setStatus(1);
        statusFMDTO.setEvent(1);
        statusFMDTO.setFrankMachineId("PM100500");
        statusFMDTO.setTaxVersion("22.1.1");
        statusFMDTO.setPostOffice("131000");
        statusFMDTO.setIsAuto(1);

        byte type = (byte) 0xB3;
        String encrypt = AESUtils.encrypt(JSON.toJSONString(statusFMDTO), FebsConstant.TEMP_KEY);
        byte[] data = BaseTypeUtils.stringToByte(encrypt, BaseTypeUtils.UTF8);

        int protocolLen = TYPE_LEN + 2 + 6 + 3 + data.length + CHECK_LEN + END_LEN;
        byte[] head = new byte[]{(byte) 0xAA};
        byte[] length = BaseTypeUtils.int2ByteArrayCons(protocolLen);
        byte[] typeData = new byte[]{type};
        byte[] operateIdData = new byte[]{(byte) 0xFF, (byte) 0XFF};
        byte[] acnumData = BaseTypeUtils.stringToByte(acnum, BaseTypeUtils.UTF8);
        byte[] versionData = BaseTypeUtils.stringToByte("001", BaseTypeUtils.UTF8);
        byte[] finalData = BaseTypeUtils.byteMerger(typeData, operateIdData);
        finalData = BaseTypeUtils.byteMerger(finalData, acnumData);
        finalData = BaseTypeUtils.byteMerger(finalData, versionData);
        finalData = BaseTypeUtils.byteMerger(finalData, data);

        byte[] checkSume = BaseTypeUtils.makeCheckSum(finalData);
        byte[] end = {(byte) 0xD0};

        int totalLen = 3 + protocolLen;

        ByteArrayOutputStream baos = new ByteArrayOutputStream(totalLen);
        baos.write(head, 0, 1);
        baos.write(length, 0, RESPONSE_LENGTH_LEN);
        baos.write(typeData, 0, TYPE_LEN);
        baos.write(operateIdData, 0, 2);
        baos.write(acnumData, 0, 6);
        baos.write(versionData, 0, 3);
        baos.write(data, 0, data.length);
        baos.write(checkSume, 0, CHECK_LEN);
        baos.write(end, 0, END_LEN);

        ByteBuf buf = Unpooled.buffer(baos.toByteArray().length);
        buf.writeBytes(baos.toByteArray());
        future.channel().writeAndFlush(buf);
        Thread.sleep(millis);
        log.info("结束ChangeStatus");
    }
}
