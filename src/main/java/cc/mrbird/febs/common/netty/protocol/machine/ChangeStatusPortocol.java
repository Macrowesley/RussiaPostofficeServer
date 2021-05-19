package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FebsConstant;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ChangeStatusPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xB4;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //返回数据长度
    private static final int RES_DATA_LEN = 1;
    
    private static final String OPERATION_NAME = "ChangeStatusPortocol";

    /**
     * 获取协议类型
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    /**
     * 解析并返回结果流
     *
     * @param bytes
     * @param ctx
     * @return
     */
    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        /*

        【使用说明】
         auth、unauth、lost、changeStatus、taxUpdate这几个操作都使用本协议
         如果是taxUpdate（更新了税率表版本）操作， event为2，其他状态操作的时候，event为1
         如果是event = 1的时候，如果是lost请求，islost = 1,否则 islost默认为0

        状态
            UNKNOWN(1,"UNKNOWN", "未知"),
            REGISTERED(2,"REGISTERED","注册"),
            AUTHORIZED(3,"AUTHORIZED","授权"),
            OPERATING(4,"OPERATING","操作的"),
            PENDING_WITHDRAWN(5,"PENDING_WITHDRAWN","待提款"),
            TEMPORARILY_WITHDRAWN(6,"TEMPORARILY_WITHDRAWN","暂时撤回"),
            PERMANENTLY_WITHDRAWN(7,"PERMANENTLY_WITHDRAWN","永久提款"),
            IN_TRANSFER(8,"IN_TRANSFER","转让中"),
            MISSING(9,"MISSING","丢失的"),
            SCRAPPED(10,"SCRAPPED","报废"),
            MAINTENENCE(11,"MAINTENENCE","维护"),
            BLOCKED(12,"BLOCKED","已封锁"),
            AUTH_CANCELED(13,"AUTH_CANCELED","取消授权"),
            DEMO(14,"DEMO","演示");

        event
        1 STATUS
        2 RATE_TABLE_UPDATE


        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length[2];				//
            unsigned char type;					//0xB4
            unsigned char acnum[6];             //机器表头号
            unsigned char version[3];             //版本号
            unsigned char content[?];			//加密后内容: StatusDTO对象的json
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))status, *status;
         */
        log.info("机器开始改变状态");
        String version = null;
        String res;
        try {
            int pos = TYPE_LEN;

            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //版本号
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;


            //下面的操作都是同步的，机器一直等着最后的结果
            switch (version) {
                case FebsConstant.FmVersion1:
                    return parseStatus(bytes, version, ctx, pos, acnum);
                default:
                    return getErrorResult(ctx, version,OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return getErrorResult(ctx, version,OPERATION_NAME);
        }

    }

    private byte[] parseStatus(byte[] bytes, String version, ChannelHandlerContext ctx, int pos, String acnum) throws Exception {
        long t1 = System.currentTimeMillis();
        StatusFMDTO statusFMDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, StatusFMDTO.class);

        log.info("解析得到的对象：statusDTO={}", statusFMDTO.toString());

        //解析参数
        String frankMachineId = statusFMDTO.getFrankMachineId();
        int statusType = statusFMDTO.getStatus();
        String postOffice = statusFMDTO.getPostOffice();
        String taxVersion = statusFMDTO.getTaxVersion();
        int eventType = statusFMDTO.getEvent();
        int isLost = statusFMDTO.getIsLost();

        FMStatusEnum status = FMStatusEnum.getByCode(statusType);
        EventEnum event = EventEnum.getByCode(eventType);

        DeviceDTO deviceDto = new DeviceDTO();
        deviceDto.setId(frankMachineId);
        deviceDto.setStatus(status);
        deviceDto.setPostOffice(postOffice);
        deviceDto.setTaxVersion(taxVersion);
        deviceDto.setEventEnum(event);
        deviceDto.setDateTime(DateKit.createRussiatime());

        //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
//        String key = ctx.channel().id().toString() + event.getEvent()  + status.getStatus();
        String key = ctx.channel().id().toString() +"_" + OPERATION_NAME;
        if (redisService.hasKey(key)){
            return getOverTimeResult(version,ctx, key, FMResultEnum.Overtime.getCode());
        }else{
            log.info("channelId={}的操作记录放入redis", key);
            redisService.set(key,"wait", WAIT_TIME);
        }

        boolean operationRes = false;
        switch (event){
            case STATUS:
                switch (status){
                    case ADD_MACHINE_INFO:
                        operationRes = serviceManageCenter.addMachineInfo(acnum, deviceDto);
                        break;
                    case AUTHORIZED:
                        operationRes = serviceManageCenter.auth(deviceDto);
                        break;
                    case AUTH_CANCELED:
                        if(isLost == 1){
                            operationRes = serviceManageCenter.lost(deviceDto);
                        }else{
                            operationRes = serviceManageCenter.unauth(deviceDto);
                        }
                        break;
                    default:
                        operationRes = serviceManageCenter.changeStatusEvent(deviceDto);
                        break;
                }
                break;
            case RATE_TABLE_UPDATE:
                operationRes = serviceManageCenter.rateTableUpdateEvent(deviceDto);
                break;
            default:
                //处理异常
                throw new FmException("状态不匹配，无法响应");
        }
        log.info("机器改变状态，通知服务器，服务器通知俄罗斯，整个过程耗时：{}",(System.currentTimeMillis() - t1));
        return getSuccessResult(version, ctx, statusType, eventType, operationRes);
    }

    private byte[] getSuccessResult(String version, ChannelHandlerContext ctx, int statusType, int eventType, boolean res) throws Exception {
        //删除redis缓存
        redisService.del(ctx.channel().id().toString());

        String responseData = (res == true? 1: 0) + version + String.valueOf(eventType) + statusType ;

        //返回内容的加密数据
        //获取临时密钥
        String tempKey = tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("改变状态：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }

}
