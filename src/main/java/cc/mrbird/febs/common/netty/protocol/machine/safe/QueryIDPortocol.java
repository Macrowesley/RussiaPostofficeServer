package cc.mrbird.febs.common.netty.protocol.machine.safe;

import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;

/**
 * 请求唯一ID
 */
@Slf4j
@NoArgsConstructor
@Component
public class QueryIDPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xA3;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //唯一id长度
    private static final int RES_UUID_LEN = 16;

    @Autowired
    IDeviceService deviceService;

    public static QueryIDPortocol queryIDPortocol;

    @PostConstruct
    public void init(){
        this.queryIDPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return queryIDPortocol;
    }

    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        try {
            int pos = getBeginPos();
            log.info("【协议】获取唯一id：  开始" + " 全部内容：" + BaseTypeUtils.byteToString(bytes, BaseTypeUtils.UTF8) + " 字节内容：" + BaseTypeUtils.bytesToHexString(bytes));
            //解析版本号
            String versionContent = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;
            int version = Integer.valueOf(versionContent);
//            log.info("version = {} byte内容 = {}", version, BaseTypeUtils.bytesToHexString(Arrays.copyOfRange(bytes, pos, pos + VERSION_LEN)));
            switch (version) {
                case 1:
                    //解析表头号
                    String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);

                    //根据acnum获取密钥
                    Device device = queryIDPortocol.deviceService.findDeviceByAcnum(acnum);
                    if (device == null) {
                        throw new Exception("获取唯一id：设备" + acnum + "不存在");
                    }

                    byte[] versionBytes = new byte[VERSION_LEN];
                    byte[] UUIDBytes = new byte[RES_UUID_LEN];

                    versionBytes = BaseTypeUtils.stringToByte(versionContent, VERSION_LEN, BaseTypeUtils.UTF8);
                    if (device.getSecretKey() != null) {
                        UUIDBytes = BaseTypeUtils.stringToByte(device.getSecretKey().trim(), RES_UUID_LEN, BaseTypeUtils.UTF8);
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(VERSION_LEN + RES_UUID_LEN);
                    baos.write(versionBytes, 0, versionBytes.length);
                    baos.write(UUIDBytes, 0, UUIDBytes.length);
                    log.info("获取唯一id：  结束 id = {} ", device.getSecretKey().trim());
                    return getWriteContent(baos.toByteArray());
                default:
                    throw new Exception("获取唯一id：版本不存在");
            }
        } catch (Exception e) {
            log.error("获取唯一id： 解析出错" + e.getMessage());
            throw new Exception(e.getMessage());
        }

        /*
         * //机器向服务器请求唯一ID
         *
         * Typedef struct{
         *     unsigned char head;                 //0xAA
         *     unsigned char length;               //
         *     unsigned char type;                 //0xA3
         *     unsigned int  operateID[2];
         *     unsigned char version[3];           //版本内容 001
         *     unsigned char acnum[6];             //机器的表头号
         *     unsigned char check;                //校验位
         *     unsigned char tail;                 //0xD0
         * }__attribute__((packed))QueryID,*QueryID;
         * 服务器返回唯一ID
         *
         * // 服务器返回唯一ID
         *
         * typedef  struct{
         *     unsigned char length;				//一个字节
         * 	unsigned char type;				 	//0xA3
         * unsigned int  operateID[2];
         * 	unsigned char version[3];           //版本内容 001
         *     unsigned char id[16];				//唯一id内容（如果都为0，则这个表头号还没注册到系统中）
         * 	unsigned char check;				//校验位
         * 	unsigned char tail;				    //0xD0
         * }__attribute__((packed))T_IdAck, *PT_IdAck;
         */
    }


}
