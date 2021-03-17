package cc.mrbird.febs.common.netty.protocol.safe;

import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.TempTimeUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * 请求临时密钥
 */
@Slf4j
@Component
public class QueryTemKeyPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xA4;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //加密内容长度
    private static final int RES_ENCRYPT_LEN = 44;

    @Autowired
    IDeviceService deviceService;

    @Autowired
    public TempTimeUtils tempTimeUtils;


    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        log.info("【协议】获取临时密钥  开始");
        int pos = TYPE_LEN;

        //解析版本号
        String versionContent = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
        pos += VERSION_LEN;
        int version = Integer.valueOf(versionContent);
        switch (version) {
            case 1:
                //解析表头号
                String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);

                //创建8位临时密钥
                //根据acnum获取密钥
                Device device = deviceService.findDeviceByAcnum(acnum);
                if (device == null) {
                    throw new Exception("获取临时密钥：设备" + acnum + "不存在");
                }
                String uuid = device.getSecretKey();
                String tempKey = AESUtils.createKey(16);
                long timestamp = System.currentTimeMillis();
                //todo 要删掉这段代码，用下面的代码，需要添加一个时间戳
                String entryptContent = AESUtils.encrypt(tempKey , uuid);
//                String entryptContent = AESUtils.encrypt(tempKey + String.valueOf(timestamp), uuid);

                //保存临时密钥
                tempKeyUtils.addTempKey(ctx, tempKey);
                //保存临时时间戳
                tempTimeUtils.addTempTime(ctx, timestamp);

                byte[] versionBytes = new byte[VERSION_LEN];
                byte[] encryptBytes = new byte[RES_ENCRYPT_LEN];

                versionBytes = BaseTypeUtils.stringToByte(versionContent, VERSION_LEN, BaseTypeUtils.UTF8);
                encryptBytes = BaseTypeUtils.stringToByte(entryptContent, RES_ENCRYPT_LEN, BaseTypeUtils.UTF8);

                ByteArrayOutputStream baos = new ByteArrayOutputStream(VERSION_LEN + RES_ENCRYPT_LEN);
                baos.write(versionBytes, 0, versionBytes.length);
                baos.write(encryptBytes, 0, encryptBytes.length);
                log.info("获取临时密钥：  结束 ， 临时密钥= {} 发给机器的是{}", tempKey, entryptContent);

                return getWriteContent(baos.toByteArray());
            default:
                throw new Exception("获取临时密钥：版本不存在");
        }
        /*2.1 请求临时加密秘钥
        Typedef struct{
            unsigned char head;                 //0xAA
            unsigned char length;               //
            unsigned char type;                 //0xA4
            unsigned char version[3];           //版本内容 001
            unsigned char acnum[6];             //机器的表头号
            unsigned char check;                //校验位
            unsigned char tail;                 //0xD0
        }__attribute__((packed))QueryInfo,*pQueryInfo;
        2.2 服务器返回临时加密秘钥

        typedef  struct{
            unsigned char length;				 //一个字节
            unsigned char head;				 	 //0xA4
            unsigned char version[3];            //版本内容 001
            unsigned char content[?];			//【改动】加密内容 临时秘钥（16）+ 时间戳（13）
            unsigned char check;				 //校验位
            unsigned char tail;					 //0xD0
        }__attribute__((packed))T_InjectionAck, *PT_InjectionAck;*/


    }
}
