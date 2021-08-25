package cc.mrbird.febs.common.netty.protocol.machine.safe;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
import cc.mrbird.febs.common.netty.protocol.kit.TempTimeUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 机器登录校验协议
 * 验证成功，加入缓存
 */
@Slf4j
@NoArgsConstructor
@Component
public class MachineLoginPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xA5;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //时间戳长度
    private static final int TIMESTAMP_LEN = 13;

    private static final String OPERATION_NAME = "MachineLoginPortocol";

    @Autowired
    IDeviceService deviceService;

    @Autowired
    TempTimeUtils tempTimeUtils;

    @Autowired
    ChannelMapperManager channelMapperManager;

    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    public static MachineLoginPortocol machineLoginPortocol;

    @PostConstruct
    public void init(){
        machineLoginPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return machineLoginPortocol;
    }

    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {

        /*
         * 发送机器表头号和时间戳，为了服务器能够通过表头号找到机器
         * 服务器通过验证时间戳的合法性（可解密，缓存中是否有这个时间戳，时间是否超时60s），可用，放入缓存
        Typedef struct{
            unsigned char head;                 //0xAA
            unsigned char length;               //
            unsigned char type;                 //0xA5
            * unsigned char  operateID[2];
            unsigned char acnum[6];             //机器的表头号
            unsigned char version[3];           //版本内容(3)
            unsigned char content[?];           //加密内容：时间戳(13)
            unsigned char check;                //校验位
            unsigned char tail;                 //0xD0
        }__attribute__((packed))machineInfo,*machineInfo;
         */
        String version = "";
        try {
            int pos = getBeginPos();
            log.info("【协议】机器登录校验协议 开始");
//            log.info("【协议】机器登录校验协议：  开始" + " 全部内容：" + BaseTypeUtils.byteToString(bytes, BaseTypeUtils.UTF8) + " 字节内容：" + BaseTypeUtils.bytesToHexString(bytes));
            //解析表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //解析版本
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;

            //加密内容
            String enctryptContent = BaseTypeUtils.byteToString(bytes, pos, bytes.length - TYPE_LEN - OPERATEID_LEN - REQ_ACNUM_LEN - VERSION_LEN - CHECK_LEN - END_LEN, BaseTypeUtils.UTF8);

            //获取临时密钥
            String tempKey = machineLoginPortocol.tempKeyUtils.getTempKey(ctx);
            log.info("enctryptContent = {} tempKey = {}", enctryptContent, tempKey);

            //解密后内容
            String dectryptContent = AESUtils.decrypt(enctryptContent, tempKey);
//            String versionContent = dectryptContent.substring(0, VERSION_LEN);

            switch (version) {
                case FebsConstant.FmVersion1:
                    pos = VERSION_LEN;

//                    String timestamp = dectryptContent.substring(pos, pos + TIMESTAMP_LEN);
                    String timestamp = dectryptContent.trim();

                    //验证时间是否正常
                    byte[] res = new byte[]{0x00};
                    if(machineLoginPortocol.tempTimeUtils.isValidTime(ctx, Long.valueOf(timestamp))){
                        res[0] = 0x01;

                        //保存到缓存
                        if (!machineLoginPortocol.channelMapperManager.containsKeyAcnum(acnum)) {
                            machineLoginPortocol.channelMapperManager.addChannel(acnum, ctx);
                        }else{
                            res[0] = 0x00;
                            log.info("有问题：服务器中保存的表头号为"+acnum + " ChannelMapperUtils.getChannelByAcnum(acnum) = "
                                    + machineLoginPortocol.channelMapperManager.getChannelByAcnum(acnum) + " 当前ctx = " + ctx );
                        }

                    }


                    //返回结果
                    /*typedef  struct{
                        unsigned char length[2];			 //2个字节
                        unsigned char type;				 	 //0xA5
                        unsigned char  operateID[2];
                        unsigned char res;                   //01 正常  00 失败 失败的话，只能重新执行请求密钥，再发送机器信息
                        unsigned char check;				 //校验位
                        unsigned char tail;					 //0xD0
                    }__attribute__((packed))T_InjectionAck, *PT_InjectionAck;*/


                    log.info("机器登录校验协议 结束");
                    return getWriteContent(res);
                default:
                    throw new Exception("获取唯一id：版本不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("机器登录校验协议： 解析出错" + e.getMessage());
            return getWriteContent(new byte[]{0x00});
        }
    }
}
