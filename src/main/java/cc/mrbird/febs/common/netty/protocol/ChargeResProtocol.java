package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChargeResProtocol extends BaseProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xA2;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //注资结果长度
    private static final int REQ_CHARGE_RES_LEN = 1;

    //机器订单ID长度
    private static final int REQ_ORDERID_LEN = 8;

    //注资金额长度
    private static final int REQ_AMOUNT_LEN = 8;

    //返回数据长度
    private static final int RES_DATA_LEN = 1;

    @Autowired
    IOrderService orderService;

    /**
     * 获取协议类型
     * A0 心跳包
     * A1 查询是否有数据包
     * A2 返回注资结果
     *
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    /**
     * 获取发送过来的协议数据部分的长度
     *
     * @return
     */
    @Override
    public int getRequestDataLen() {
        return REQ_ACNUM_LEN + REQ_CHARGE_RES_LEN + REQ_ORDERID_LEN + REQ_AMOUNT_LEN;
    }

    /**
     * 获取返回的协议数据部分的长度
     *
     * @return
     */
    @Override
    public int getResponsetDataLen() {
        return RES_DATA_LEN;
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
        //解析
        /*typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length;				//0x09
            unsigned char type;					//0xA2
            unsigned char acnum[6];			    //机器的表头号
            unsigned char result;				//0x00 注资失败  0x01 注资成功
            unsigned long  orderId;				//机器订单ID
            unsigned long  amount				//注资金额
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))Result,*Result;*/

        log.info("机器返回结果");
        try {
            //0xa2,0x43,0x50,0x55,0x31,0x32,0x33,0x01,0x22,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x38,0x31,0x00,0x00,0x00,0x00,0x00,0x00,0xac,0xd0,
            int pos = TYPE_LEN;

            //解析表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //0x00 注资失败  0x01 注资成功
            byte resByte = bytes[pos];
            pos += REQ_CHARGE_RES_LEN;
            boolean chargeRes = resByte == 0x01;

            //机器订单ID
            long orderId = BaseTypeUtils.byte2Long(bytes, pos, REQ_ORDERID_LEN);
            pos += REQ_ORDERID_LEN;

            //注资金额（分）
            long reqAmount = BaseTypeUtils.byte2Long(bytes, pos, REQ_AMOUNT_LEN);
            pos += REQ_AMOUNT_LEN;

            //注资金额（元）
            String amount = MoneyUtils.changeF2Y(reqAmount);

            //验证
            if (acnum.trim().length() != 6) {
                throw new Exception("表头号不正确");
            }

            //更新订单状态
            OrderVo orderVo = new OrderVo();
            orderVo.setAcnum(acnum);
            orderVo.setOrderId(orderId);
            orderVo.setAmount(amount);
            orderService.updateMachineInjectionStatus(orderVo, chargeRes);
            log.info("机器返回结果 更新订单状态");

        }catch (Exception e){
            e.printStackTrace();
        }
        //返回数据
        byte[] data = new byte[]{(byte) 0x02};
        return getWriteContent(data);
    }
}
