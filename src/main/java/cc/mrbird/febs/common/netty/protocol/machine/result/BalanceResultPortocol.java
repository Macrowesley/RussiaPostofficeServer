package cc.mrbird.febs.common.netty.protocol.machine.result;

import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@NoArgsConstructor
@Component
public class BalanceResultPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xC5;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    private static final int REQ_UPDATE_TAXES_RES_LEN = 1;


    public static BalanceResultPortocol balanceResultPortocol;

    @PostConstruct
    public void init(){
        balanceResultPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return balanceResultPortocol;
    }

    /**
     * 获取协议类型
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    @Override
    public String getProtocolName() {
        return "BalanceResultPortocol";
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
        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length[4];				//
            unsigned char type;					//0xC5
            unsigned char  operateID[2];
            unsigned char acnum[?];             //frankMachineId
            unsigned char result;				//加密内容：balanceId() + result(1)
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))updateTaxesRes, *updateTaxesRes;
         */
        int pos = getBeginPos();

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //结果
        String res = bytes[pos] == 0x01 ?"成功":"失败";

        log.info("{}机器同步金额结果：{}", acnum, res);


        //返回 todo 返回需要写清楚点
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
