package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.machine.DTO.StatusDTO;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.rcs.common.enums.EventEnum;
import cc.mrbird.febs.rcs.common.enums.FMStatusEnum;
import cc.mrbird.febs.rcs.dto.manager.FrankMachineDTO;
import com.alibaba.fastjson.JSON;
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
    public byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
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
            unsigned char content[?];			//加密后内容: StatusDTO对象的json
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))auth, *auth;
         */
        int pos = TYPE_LEN;

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //加密内容解密
        String decryptContent = getDecryptContent(bytes, ctx, pos, REQ_ACNUM_LEN);
        log.info("解析得到的内容：decryptContent={}",decryptContent);

        StatusDTO statusDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN,StatusDTO.class);
        log.info("解析得到的对象：statusDTO={}",statusDTO.toString());

        //解析参数
        String version = "";
        String frankMachineId = "";
        String statusStr = "";
        String postOffice = "";
        String taxVersion = "";
        int eventType = 0;
        int isLost = 0;

        FMStatusEnum status = FMStatusEnum.getByStatus(statusStr);
        EventEnum event = EventEnum.getEventByType(eventType);

        FrankMachineDTO device = new FrankMachineDTO();
        device.setId(frankMachineId);
        device.setStatus(status);
        device.setPostOffice(postOffice);
        device.setTaxVersion(taxVersion);
        device.setEventEnum(event);

        switch (event){
            case STATUS:

                switch (status){
                    case AUTHORIZED:
                        serviceManageCenter.auth(device);
                        break;
                    case AUTH_CANCELED:
                        if(isLost == 1){
                            serviceManageCenter.lost(device);
                        }else{
                            serviceManageCenter.unauth(device);
                        }
                        break;
                    default:
                        //todo 这个情况怎么处理
                        serviceManageCenter.changeStatusEvent(device);
                        break;
                }

                break;
            case RATE_TABLE_UPDATE:
                serviceManageCenter.rateTableUpdateEvent(device);
                break;
            default:
                //处理异常
                //todo 这个情况怎么处理
                break;
        }



        //返回 todo 返回需要写清楚点
        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char head;				 	 //0xB4
         unsigned char content[?];				 // 加密内容  event(1) + status(1) + result(1)
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))auth, *auth;
         */
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }


}
