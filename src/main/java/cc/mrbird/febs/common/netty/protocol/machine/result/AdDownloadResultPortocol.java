package cc.mrbird.febs.common.netty.protocol.machine.result;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionFMDTO;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.dto.machine.AdImageRes;
import cc.mrbird.febs.rcs.dto.machine.AdInfoResDTO;
import cc.mrbird.febs.rcs.entity.AdImage;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.service.IAdImageService;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * 机器广告下载结果
 */
@Slf4j
@NoArgsConstructor
@Component
public class AdDownloadResultPortocol extends MachineToServiceProtocol {
    @Autowired
    IAdImageService adImageService;

    public static final byte PROTOCOL_TYPE = (byte) 0xCB;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    private static final String OPERATION_NAME = "AdDownloadResultPortocol";



    public static AdDownloadResultPortocol portocol;

    @PostConstruct
    public void init(){
        portocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return portocol;
    }

    /**
     * 获取协议类型
     *
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    @Override
    public String getProtocolName() {
        return OPERATION_NAME;
    }

    /**
     * 解析并返回结果流
     *
     * @param bytes
     * @param ctx
     * @return
     */
    @Override
    public byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        String version = null;
        String acnum = "";
        try {
            /*
            typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[4];
                unsigned char type;					//0xCB
                unsigned char operateID[2];
                unsigned char acnum[6];             //机器表头号
                unsigned char version[3];           //版本号
                unsigned char result;				//AdInfoResDTO的json
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))AdInfoRes, *AdInfoRes;
             */

            int pos = getBeginPos();

            //表头号
            acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //版本号
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;

            switch (version) {
                case FebsConstant.FmVersion1:
                    AdInfoResDTO adInfoResDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, AdInfoResDTO.class);
                    log.info("处理机器下载广告结果： 解析得到的对象 adInfoResDTO={}", adInfoResDTO.toString());

                    ArrayList<AdImage> adImageList = new ArrayList<>();
                    //解析机器广告图片下载情况，更新数据库
                    AdImageRes[] adImageArr = adInfoResDTO.getAdImageList();
                    for (int i = 0; i < adImageArr.length; i++) {
                        AdImageRes bean = adImageArr[i];

                        AdImage adImage = new AdImage();
                        adImage.setId(Integer.valueOf(bean.getImageId()));
                        adImage.setStatus(Integer.valueOf(bean.getRes()));

                        adImageList.add(adImage);
                    }
                    //批量更新
                    portocol.adImageService.updateBatchById(adImageList, adImageList.size());

                    byte[] data = new byte[]{(byte) 0x01};
                    return getWriteContent(data);
                default:
                    return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }

        } catch (Exception e) {
            log.error(OPERATION_NAME + " error info = " + e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME);
        }
    }
}
