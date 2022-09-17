package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.StatusFMDTO;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.rcs.common.enums.EventEnum;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FMStatusEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.manager.DeviceDTO;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@NoArgsConstructor
@Component
public class ChangeStatusPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xB4;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    private static final String OPERATION_NAME = "ChangeStatusPortocol";

    public static ChangeStatusPortocol changeStatusPortocol;

    @PostConstruct
    public void init(){
        changeStatusPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return changeStatusPortocol;
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
        String version = null;
        String res;
        try {
            int pos = getBeginPos();

            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //版本号
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;


            //下面的操作都是同步的，机器一直等着最后的结果
            switch (version) {
                case FebsConstant.FmVersion1:
                    long t1 = System.currentTimeMillis();
                    StatusFMDTO statusFMDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, StatusFMDTO.class);

                    log.info("解析得到的对象：statusDTO={}", statusFMDTO.toString());
                    if (StringUtils.isEmpty(statusFMDTO.getFrankMachineId())) {
                        return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.SomeInfoIsEmpty.getCode());
                    }

                    //解析参数
                    String frankMachineId = statusFMDTO.getFrankMachineId();
                    int statusType = statusFMDTO.getStatus();
                    String postOffice = statusFMDTO.getPostOffice();
                    String taxVersion = statusFMDTO.getTaxVersion();
                    int eventType = statusFMDTO.getEvent();
                    //是否是机器主动改变状态
                    boolean isMachineActive = statusFMDTO.getIsAuto() == 1;

                    FMStatusEnum status = FMStatusEnum.getByCode(statusType);
                    EventEnum event = EventEnum.getByCode(eventType);

                    DeviceDTO deviceDto = new DeviceDTO();
                    deviceDto.setId(frankMachineId);
                    deviceDto.setStatus(status);
                    deviceDto.setPostOffice(postOffice);
                    deviceDto.setTaxVersion(taxVersion);
                    deviceDto.setEvent(event);
                    deviceDto.setDateTime(DateKit.createRussiatime());

                    //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
                    String key = ctx.channel().id().toString() + "_" + OPERATION_NAME;
                    if (changeStatusPortocol.redisService.hasKey(key)) {
                        return getOverTimeResult(version, ctx, key, FMResultEnum.Overtime.getCode());
                    } else {
                        log.info("channelId={}的操作记录放入redis", key);
                        changeStatusPortocol.redisService.set(key, "wait", WAIT_TIME);
                    }

                    switch (event) {
                        case STATUS:
                            if (status == FMStatusEnum.ADD_MACHINE_INFO){
                                //机器注册信息到服务器
                                changeStatusPortocol.serviceManageCenter.addMachineInfo(acnum, deviceDto);
                            }else{
                                if (isMachineActive){
                                    switch (status) {
                                        case ENABLED:
                                            changeStatusPortocol.serviceManageCenter.auth(deviceDto);
                                            break;
                                        case UNAUTHORIZED:
                                            changeStatusPortocol.serviceManageCenter.unauth(deviceDto);
                                            break;
                                        case LOST:
                                            changeStatusPortocol.serviceManageCenter.lost(deviceDto);
                                            break;
                                        default:
                                            changeStatusPortocol.serviceManageCenter.changeStatusEvent(deviceDto, isMachineActive);
                                            break;
                                    }
                                }else{
                                    changeStatusPortocol.serviceManageCenter.changeStatusEvent(deviceDto, isMachineActive);
                                }
                            }
                            break;
                        case RATE_TABLE_UPDATE:
                            //这里应该是作废的，目前是在开机的时候，机器发送版本号给服务器，服务器来判断是否需要发给俄罗斯，代码先留着看看
                            changeStatusPortocol.serviceManageCenter.frankMachinesRateTableUpdateEvent(deviceDto);
                            break;
                        default:
                            //处理异常
                            log.error("event 不匹配，无法响应");
                            throw new FmException("状态不匹配，无法响应");
                    }
                    log.info("ChangeStatusPortocol 整个过程耗时：{}", (System.currentTimeMillis() - t1));
                    return getSuccessResult(version, ctx, statusType, eventType);
                default:
                    return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }
        } catch (FmException e) {
//            e.printStackTrace();
            log.error(OPERATION_NAME + " FmException info = " + e.getMessage());
            if (-1 != e.getCode()) {
                return getErrorResult(ctx, version, OPERATION_NAME, e.getCode());
            } else {
                return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
        }

    }

    private byte[] getSuccessResult(String version, ChannelHandlerContext ctx, int statusType, int eventType) throws Exception {
        //删除redis缓存
        changeStatusPortocol.redisService.del(ctx.channel().id().toString());

        String responseData = FMResultEnum.SUCCESS.getSuccessCode() + version + String.valueOf(eventType) + statusType;

        //返回内容的加密数据
        //获取临时密钥
        String tempKey = changeStatusPortocol.tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("改变状态：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }

}
