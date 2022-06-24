package cc.mrbird.febs.common.netty.protocol.machine.safe;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
import cc.mrbird.febs.common.netty.protocol.kit.TempTimeUtils;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.dto.machine.AdImageInfo;
import cc.mrbird.febs.rcs.dto.machine.AdInfoDTO;
import cc.mrbird.febs.rcs.service.IAdImageService;
import com.alibaba.fastjson.JSON;
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

    @Autowired
    IAdImageService adImageService;

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
    public String getProtocolName() {
        return OPERATION_NAME;
    }

    @Override
    public byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {

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
            unsigned char content[?];           //加密内容：时间戳(13) + frankMachineId(9)
            unsigned char check;                //校验位
            unsigned char tail;                 //0xD0
        }__attribute__((packed))machineInfo,*machineInfo;
         */
        String version = "";
        try {
            int pos = getBeginPos();
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
                    String timestamp = dectryptContent.trim().substring(0,13);
                    String frankMachineId = dectryptContent.trim().substring(13 );
                    log.info("timestamp={}, frankMachineId={}",timestamp, frankMachineId);

                    //验证时间是否正常
//                    byte[] res = new byte[]{0x00};
                    String res = "0";
                    String returnData = "";
                    if(machineLoginPortocol.tempTimeUtils.isValidTime(ctx, Long.valueOf(timestamp))){
                        res = "1";

                        //保存到缓存
                        if (!machineLoginPortocol.channelMapperManager.containsKeyAcnum(acnum)) {
                            machineLoginPortocol.channelMapperManager.addChannel(acnum, ctx);

                            //把机器广告图片列表给机器
                            AdImageInfo[] adImageInfoArr = machineLoginPortocol.adImageService.getAdImageInfoArr(frankMachineId);
                            AdInfoDTO adInfoDTO = new AdInfoDTO();
                            adInfoDTO.setAdImageList(adImageInfoArr);
                            returnData = JSON.toJSONString(adInfoDTO);
                        }else{
                            res = "0";
                            log.info("有问题：服务器中保存的表头号为"+acnum + " ChannelMapperUtils.getChannelByAcnum(acnum) = "
                                    + machineLoginPortocol.channelMapperManager.getChannelByAcnum(acnum) + " 当前ctx = " + ctx );
                        }

                    } else {
                        log.error("发送过来的时间戳无效");
                    }


                    //返回结果
                    /*typedef  struct{
                        unsigned char length[4];			 //2个字节
                        unsigned char type;				 	 //0xA5
                        unsigned char  operateID[2];
                        unsigned char res;                   //加密内容： 1 正常  0 失败 (失败的话，发给机器0x00) + AdInfoDTO的json
                        unsigned char check;				 //校验位
                        unsigned char tail;					 //0xD0
                    }__attribute__((packed))T_InjectionAck, *PT_InjectionAck;*/

                    String content = res + returnData;
                    String resEntryctContent = AESUtils.encrypt(content, tempKey);
                    log.info("机器登录校验协议 结束");



                    return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
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
