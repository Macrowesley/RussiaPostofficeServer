package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.service.IOrderService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Slf4j
@Component
public class QueryProtocol extends BaseProtocol {
    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //查询结果长度
    private static final int RES_RESULT_LEN = 1;
    //机器订单ID长度
    private static final int RES_ORDERID_LEN = 8;
    //注资金额长度
    private static final int RES_AMOUNT_LEN = 8;
    //协议类型
    public static final byte PROTOCOL_TYPE = (byte) 0xA1;

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
        return REQ_ACNUM_LEN;
    }

    /**
     * 获取返回的协议数据部分的长度
     *
     * @return
     */
    @Override
    public int getResponsetDataLen() {
        return RES_RESULT_LEN + RES_ORDERID_LEN + RES_AMOUNT_LEN;
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
        int pos = TYPE_LEN;

        //解析协议
        /*typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length;				//0x09
            unsigned char type;					//0xA1
            unsigned char acnum[6];			    //机器的表头号
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))QueryData,*QueryData;*/

        //解析表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        if (acnum.trim().length() != 6){
            throw new Exception("表头号不正确");
        }

        byte[] resultBytes = new byte[RES_RESULT_LEN];
        byte[] orderIdBytes = new byte[RES_ORDERID_LEN];
        byte[] amountBytes = new byte[RES_AMOUNT_LEN];

        Order order = orderService.machineRequestData(acnum);

        if (order != null){
            resultBytes[0] = 0x01;
            orderIdBytes = BaseTypeUtils.long2Bytes(order.getOrderId());
            //订单金额 元
            String amount = order.getAmount();
            long finalAmount = MoneyUtils.changeY2F(Double.valueOf(amount));
            amountBytes = BaseTypeUtils.long2Bytes(finalAmount);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(getResponsetDataLen());
        baos.write(resultBytes, 0, RES_RESULT_LEN);
        baos.write(orderIdBytes, 0, RES_ORDERID_LEN);
        baos.write(amountBytes, 0, RES_AMOUNT_LEN);

        //拼接数据，返回给客户端
        /*typedef  struct{
            unsigned char length;				 	//一个字节
            unsigned char type;				 	    //0xA1
            unsigned char result;				    //0x00 无结果  0x01 有结果
            unsigned long  orderId;				 	//机器订单ID
            unsigned long  amount				 	//注资金额
            unsigned char check;				 	//校验位
            unsigned char tail;					 	//0xD0
        }__attribute__((packed))T_InjectionAck, *PT_InjectionAck;*/

        return getWriteContent(baos.toByteArray());
    }
}
