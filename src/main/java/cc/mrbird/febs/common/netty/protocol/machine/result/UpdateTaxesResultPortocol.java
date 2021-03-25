package cc.mrbird.febs.common.netty.protocol.machine.result;

import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateTaxesResultPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xC4;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    private static final int REQ_UPDATE_TAXES_RES_LEN = 1;

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
            unsigned char length;				//0x09
            unsigned char type;					//0xC4
            unsigned char acnum[?];             //frankMachineId
            unsigned char result;				//0x00 失败  0x01 成功
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))updateTaxesRes, *updateTaxesRes;
         */
        int pos = TYPE_LEN;

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //结果
        String res = BaseTypeUtils.byteToString(bytes, pos, REQ_UPDATE_TAXES_RES_LEN, BaseTypeUtils.UTF8);

        log.info("{}机器的税率表更新结果：{}", acnum, res.equals("1")? "成功":"失败");

        //todo 统计所有邮资机收到资费表的状态


        //返回 todo 返回需要写清楚点
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
