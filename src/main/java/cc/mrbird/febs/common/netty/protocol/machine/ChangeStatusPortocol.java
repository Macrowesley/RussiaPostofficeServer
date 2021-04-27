package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FMResultEnum;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.StatusDTO;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.rcs.common.enums.EventEnum;
import cc.mrbird.febs.rcs.common.enums.FMStatusEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.dto.manager.DeviceDTO;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeStatusPortocol extends MachineToServiceProtocol {
    @Autowired
    RedisService redisService;

    //预计一次操作的最长等待时间
    public static final long WAIT_TIME = 60L;

    public static final byte PROTOCOL_TYPE = (byte) 0xB4;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;



    //返回数据长度
    private static final int RES_DATA_LEN = 1;

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
            unsigned char length;				//0x09
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
                case "001":
                    return parseStatus(bytes, version, ctx, pos);
                default:
                    return getErrorResult(ctx, version);
            }
        }catch (Exception e){
            return getErrorResult(ctx, version);
        }

    }

    private byte[] parseStatus(byte[] bytes, String version, ChannelHandlerContext ctx, int pos) throws Exception {
        long t1 = System.currentTimeMillis();
        StatusDTO statusDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, StatusDTO.class);
        log.info("解析得到的对象：statusDTO={}", statusDTO.toString());

        //解析参数
        String frankMachineId = statusDTO.getFrankMachineId();
        int statusType = statusDTO.getStatus();
        String postOffice = statusDTO.getPostOffice();
        String taxVersion = statusDTO.getTaxVersion();
        int eventType = statusDTO.getEvent();
        int isLost = statusDTO.getIsLost();

        FMStatusEnum status = FMStatusEnum.getByCode(statusType);
        EventEnum event = EventEnum.getByCode(eventType);

        DeviceDTO device = new DeviceDTO();
        device.setId(frankMachineId);
        device.setStatus(status);
        device.setPostOffice(postOffice);
        device.setTaxVersion(taxVersion);
        device.setEventEnum(event);

        //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
//        String key = ctx.channel().id().toString() + event.getEvent()  + status.getStatus();
        String key = ctx.channel().id().toString();
        if (redisService.hasKey(key)){
            return getOverTimeResult(version,ctx, FMResultEnum.Overtime.getCode());
        }else{
            log.info("channelId={}的操作记录放入redis", key);
            redisService.set(key,"wait", WAIT_TIME);
        }

        boolean operationRes = false;
        switch (event){
            case STATUS:
                switch (status){
                    case AUTHORIZED:
                        operationRes = serviceManageCenter.auth(device);
                        break;
                    case AUTH_CANCELED:
                        if(isLost == 1){
                            operationRes = serviceManageCenter.lost(device);
                        }else{
                            operationRes = serviceManageCenter.unauth(device);
                        }
                        break;
                    default:
                        operationRes = serviceManageCenter.changeStatusEvent(device);
                        break;
                }
                break;
            case RATE_TABLE_UPDATE:
                operationRes = serviceManageCenter.rateTableUpdateEvent(device);
                break;
            default:
                //处理异常
                throw new FmException("状态不匹配，无法响应");
        }
        log.info("机器改变状态，通知服务器，服务器通知俄罗斯，整个过程耗时：{}",(System.currentTimeMillis() - t1));
        return getSuccessResult(version, ctx, statusType, eventType, operationRes);
    }

    private byte[] getErrorResult(ChannelHandlerContext ctx, String version) throws Exception {
        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char head;				 	 //0xB4
         unsigned char content[?];				 //加密内容:   result(1 为1,操作成功，则后面再添加几个参数，可以作为验证) + 版本内容(3) + event(1) + status(1)
                                                              result(1 为0,操作失败) + 版本内容(3)
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))status, *status;
         */
        //删除redis缓存
        redisService.del(ctx.channel().id().toString());

        //返回内容的原始数据
        String responseData = FMResultEnum.FAIL.getCode() + version;

        //test
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setEvent(1);
        statusDTO.setFrankMachineId("myId");
        statusDTO.setPostOffice("hello world");
        responseData = JSON.toJSONString(statusDTO);
        log.info("json = {}", responseData);

        //返回内容的加密数据
        //获取临时密钥
        String tempKey = tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("改变状态：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }

    private byte[] getSuccessResult(String version, ChannelHandlerContext ctx, int statusType, int eventType, boolean res) throws Exception {
        //删除redis缓存
        redisService.del(ctx.channel().id().toString());

        String responseData = res + version + String.valueOf(eventType) + statusType ;

        //返回内容的加密数据
        //获取临时密钥
        String tempKey = tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("改变状态：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }

    /**
     * 指定时间内多次请求返回结果
     * @param version
     * @param ctx
     * @param res
     * @return
     * @throws Exception
     */
    private byte[] getOverTimeResult(String version, ChannelHandlerContext ctx, int res) throws Exception {
        log.info("指定时间内多次请求返回结果");
        String responseData = res + version ;
        //返回内容的加密数据
        //获取临时密钥
        String tempKey = tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }
}
