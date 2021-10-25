package cc.mrbird.febs.common.netty.test;

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

@Slf4j
@Component
@NoArgsConstructor
public class TestMachineLogin implements Runnable{

    /**
     * 1、checkService
     * 2、获取临时密钥
     * 3、登录
     */
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

    public TestMachineLogin(int pos){
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

            testCheckService(future,acnum,foreseenId,transactionId,msgCount+1,millis);
            testQueryTemKey(future);
            testMachineLogin(future, acnum, millis);



            Thread.sleep(3000);
            //当通道关闭了，就继续往下走
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("客户端结束");
    }

    /*
    checkService测试
     */
    private static void testCheckService(ChannelFuture future, String acnum, String foreseenId, String transactionId, int msgCount, long millis) throws InterruptedException {

        /*
            作用：
             1. 请求服务器返回最新状态
             2. 返回上一次打印任务信息

            typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[4];			//
                unsigned char type;					//0xB3
                unsigned char  operateID[2];
                unsigned char acnum[6];             //机器表头号
                unsigned char version[3];           //版本号
                unsigned char content[?];			//加密后内容: CheckServiceDTO 对象的json
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))CheckService, *CheckService;
         */


        log.info("开始CheckService");

        CheckServiceDTO checkServiceDTO =new CheckServiceDTO();
        checkServiceDTO.setFrankMachineId(frankMachineId);
        checkServiceDTO.setTaxVersion(taxVersion);
        TransactionMsgFMDTO transactionMsgFMDTO = new TransactionMsgFMDTO();
        transactionMsgFMDTO.setId(msgCount == 1 ? foreseenId : transactionId);
        transactionMsgFMDTO.setTestId(transactionId);
        transactionMsgFMDTO.setIdType(msgCount == 1 ? 1 : 2);
        transactionMsgFMDTO.setTotalCount(totalCount.toString());
        transactionMsgFMDTO.setTotalAmount(totalAmmount);
        transactionMsgFMDTO.setDmMsg("!45!01NE6431310002803001005000000000500000000000000100001010");//60字节
        transactionMsgFMDTO.setStatus(msgCount % 2 == 1? "1":"2");
        transactionMsgFMDTO.setFrankMachineId(frankMachineId);
        checkServiceDTO.setDmMsgDto(transactionMsgFMDTO);

        byte type = (byte) 0xB3;
        String encrypt = AESUtils.encrypt(JSON.toJSONString(checkServiceDTO), FebsConstant.TEMP_KEY);
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
        log.info("结束CheckService");
    }


    /*
    机器登录测试
     */
    private static void testMachineLogin(ChannelFuture future, String acnum, long millis) throws InterruptedException {
        /*
         * 发送机器表头号和时间戳，为了服务器能够通过表头号找到机器
         * 服务器通过验证时间戳的合法性，可解密 + 不超过一分钟就可用，放入缓存
        Typedef struct{
            unsigned char head;                 //0xAA
            unsigned char length;               //
            unsigned char type;                 //0xA5
            unsigned char operateID[2];
            unsigned char acnum[6];             //机器的表头号
            unsigned char version[3];           //版本内容(3)
            unsigned char content[?];           //加密内容：时间戳(13)
            unsigned char check;                //校验位
            unsigned char tail;                 //0xD0
        }__attribute__((packed))machineInfo,*machineInfo;
        */
        log.info("开始MachineLogin");

        String timestamp = "0000000000000";

        byte type = (byte) 0xA5;
        String encrypt = AESUtils.encrypt(timestamp,FebsConstant.TEMP_KEY);
        byte[] data = BaseTypeUtils.stringToByte(encrypt,BaseTypeUtils.UTF8);

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
        log.info("结束MachineLogin");
    }

    /*
    获取临时密钥
     */
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
