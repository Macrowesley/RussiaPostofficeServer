package cc.mrbird.febs.common.netty.protocol.machine.safe;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
import cc.mrbird.febs.common.netty.protocol.kit.TempTimeUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;

/**
 * 请求临时密钥
 */
@Slf4j
@NoArgsConstructor
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
    TempTimeUtils tempTimeUtils;

    @Autowired
    ChannelMapperManager channelMapperManager;

    public static QueryTemKeyPortocol queryTemKeyPortocol;

    @PostConstruct
    public void init(){
        this.queryTemKeyPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return queryTemKeyPortocol;
    }

    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    String OPERATION_NAME = "QueryTemKeyPortocol";

    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        log.info("【协议】获取临时密钥  开始");
        int pos = getBeginPos();

        //解析版本号
        String version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
        pos += VERSION_LEN;
        switch (version) {
            case FebsConstant.FmVersion1:
                //解析表头号
                String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);

                //判断机器是否登录成功
                if (queryTemKeyPortocol.channelMapperManager.containsKeyAcnum(acnum)) {
                    return getErrorResult(ctx, version,OPERATION_NAME, FMResultEnum.MachineLogined.getCode());
                }
                //创建8位临时密钥
                //根据acnum获取密钥
                Device device = queryTemKeyPortocol.deviceService.findDeviceByAcnum(acnum);
                if (device == null) {
                    log.error("获取临时密钥：设备" + acnum + "不存在");
                    return getErrorResult(ctx, version,OPERATION_NAME, FMResultEnum.DeviceNotFind.getCode());
                }
                String uuid = device.getSecretKey();
                String tempKey = AESUtils.createKey(16);
                long timestamp = System.currentTimeMillis();
                //todo 要删掉这段代码，用下面的代码，需要添加一个时间戳
//                String entryptContent = AESUtils.encrypt(tempKey , uuid);
                String content = tempKey + String.valueOf(timestamp);
                String entryptContent = AESUtils.encrypt(content, uuid);


                //保存临时密钥
                queryTemKeyPortocol.tempKeyUtils.addTempKey(ctx, tempKey);
                //保存临时时间戳
                queryTemKeyPortocol.tempTimeUtils.addTempTime(ctx, timestamp);

                byte[] versionBytes = new byte[VERSION_LEN];
                byte[] encryptBytes = new byte[RES_ENCRYPT_LEN];

                versionBytes = BaseTypeUtils.stringToByte(version, VERSION_LEN, BaseTypeUtils.UTF8);
                encryptBytes = BaseTypeUtils.stringToByte(entryptContent, RES_ENCRYPT_LEN, BaseTypeUtils.UTF8);

                ByteArrayOutputStream baos = new ByteArrayOutputStream(VERSION_LEN + RES_ENCRYPT_LEN);
                baos.write(versionBytes, 0, versionBytes.length);
                baos.write(encryptBytes, 0, encryptBytes.length);
                log.info("获取临时密钥：  结束 ， 临时密钥= {} content = {} 发给机器的是{}", tempKey, content, entryptContent);

                return getWriteContent(baos.toByteArray());
            default:
                throw new Exception("获取临时密钥：版本不存在");
        }
        /*2.1 请求临时加密秘钥
        Typedef struct{
            unsigned char head;                 //0xAA
            unsigned char length;               //
            unsigned char type;                 //0xA4
            unsigned int  operateID[2];
            unsigned char version[3];           //版本内容 001
            unsigned char acnum[6];             //机器的表头号
            unsigned char check;                //校验位
            unsigned char tail;                 //0xD0
        }__attribute__((packed))QueryInfo,*pQueryInfo;
        2.2 服务器返回临时加密秘钥

        typedef  struct{
            unsigned char length[2];				 //2个字节
            unsigned char head;				 	 //0xA4
            unsigned char version[3];            //版本内容 001
            unsigned char content[?];			//【改动】加密内容 临时秘钥（16）+ 时间戳（13）
            unsigned char check;				 //校验位
            unsigned char tail;					 //0xD0
        }__attribute__((packed))T_InjectionAck, *PT_InjectionAck;*/


    }
}
