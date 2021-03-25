package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.asu.entity.enums.Event;
import cc.mrbird.febs.asu.entity.enums.FMStatus;
import cc.mrbird.febs.asu.entity.manager.FrankMachine;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UnAuthPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xB2;

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
        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length;				//0x0 ?
            unsigned char type;					//0xB2
            unsigned char acnum[6];             //机器表头号
            unsigned char content[?];			//加密后内容: 版本内容（长度3） + todo 机器信息（）？
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))ssh, *ssh;
         */
        int pos = TYPE_LEN;

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //todo 解析得到机器信息
        FrankMachine machine = new FrankMachine();
        machine.setId("");
        machine.setDateTime("");
        machine.setStatus(FMStatus.ENABLED);
        machine.setPostOffice("");
        machine.setTaxVersion("");
        //校验事件
        int eventType = 0;
        Event event = Event.getEventByType(eventType);
        if (event == null){
            //返回 todo 返回需要写清楚点
            byte[] data = new byte[]{(byte) 0xFF};
            return data;
        }
        machine.setEvent(event);

        serviceManageCenter.unauth(machine);


        //返回 todo 返回需要写清楚点
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
