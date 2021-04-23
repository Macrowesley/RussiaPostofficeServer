package cc.mrbird.febs.common.netty.protocol.machine.safe;

import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperUtils;
import cc.mrbird.febs.common.netty.protocol.kit.TempTimeUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.device.service.IDeviceService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 机器登录校验协议
 * 验证成功，加入缓存
 */
@Slf4j
@Component
public class MachineLoginPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xA5;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //时间戳长度
    private static final int TIMESTAMP_LEN = 13;

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

        /*
         * 发送机器表头号和时间戳，为了服务器能够通过表头号找到机器
         * 服务器通过验证时间戳的合法性（可解密，缓存中是否有这个时间戳，时间是否超时60s），可用，放入缓存
        Typedef struct{
            unsigned char head;                 //0xAA
            unsigned char length;               //
            unsigned char type;                 //0xA5
            unsigned char acnum[6];             //机器的表头号
            unsigned char content[?];           //版本内容(3) + 时间戳(13)
            unsigned char check;                //校验位
            unsigned char tail;                 //0xD0
        }__attribute__((packed))machineInfo,*machineInfo;
         */
        try {
            int pos = TYPE_LEN;
            log.info("【协议】机器登录校验协议 开始");
//            log.info("【协议】机器登录校验协议：  开始" + " 全部内容：" + BaseTypeUtils.byteToString(bytes, BaseTypeUtils.UTF8) + " 字节内容：" + BaseTypeUtils.bytesToHexString(bytes));
            //解析表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //加密内容
            String enctryptContent = BaseTypeUtils.byteToString(bytes, pos, bytes.length - TYPE_LEN - REQ_ACNUM_LEN - CHECK_LEN - END_LEN, BaseTypeUtils.UTF8);

            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(ctx);

            //解密后内容
            String dectryptContent = AESUtils.decrypt(enctryptContent, tempKey);
            String versionContent = dectryptContent.substring(0, VERSION_LEN);
            //解析版本号
            int version = Integer.valueOf(versionContent);
            switch (version) {
                case 1:
                    pos = VERSION_LEN;

                    String timestamp = dectryptContent.substring(pos, pos + TIMESTAMP_LEN);

                    //验证时间是否正常
                    byte[] res = new byte[0x00];
                    if(tempTimeUtils.isValidTime(ctx, Long.valueOf(timestamp))){
                        res[0] = 0x01;

                        //保存到缓存
                        ChannelMapperUtils.addChannel(acnum, ctx);
                    }


                    //返回结果
                    /*typedef  struct{
                        unsigned char length;				 //一个字节
                        unsigned char head;				 	 //0xA5
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
            log.error("机器登录校验协议： 解析出错" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
