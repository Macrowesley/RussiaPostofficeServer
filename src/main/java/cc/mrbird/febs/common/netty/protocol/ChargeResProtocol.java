package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 机器返回注资结果
 */
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
     * 解析并返回结果流
     *
     * @param bytes
     * @param ctx
     * @return
     */
    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        try {
            /*typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length;				//0x09
                unsigned char type;					//0xA2
                unsigned char acnum[6];             //机器表头号
                unsigned char content[?];           //加密后内容 版本内容(3) + 注资结果（1）+ 机器订单ID（8）+ 注资金额（8）
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))Result,*Result;*/
            int pos = TYPE_LEN;
            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //加密内容
            String enctryptContent = BaseTypeUtils.byteToString(bytes, pos, bytes.length - TYPE_LEN - REQ_ACNUM_LEN - CHECK_LEN - END_LEN, BaseTypeUtils.UTF8);

            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(acnum + getCID(ctx));

            //解密后内容
            String dectryptContent = AESUtils.decrypt(enctryptContent, tempKey);

            String versionContent = dectryptContent.substring(0, VERSION_LEN);
            pos = VERSION_LEN;

            log.info("机器返回注资结果： 解析加密内容，version={}, acnum={}", versionContent, acnum);

            int version = Integer.valueOf(versionContent);
            switch (version) {
                case 1:
                    //表头号
                    pos = VERSION_LEN;


                    //注资结果
                    String chargeRes = dectryptContent.substring(pos, pos + REQ_CHARGE_RES_LEN);
                    pos += REQ_CHARGE_RES_LEN;

                    //机器订单ID
                    String orderId = dectryptContent.substring(pos, pos + REQ_ORDERID_LEN);
                    pos += REQ_ORDERID_LEN;

                    //注资金额
                    String amount = dectryptContent.substring(pos, pos + REQ_AMOUNT_LEN);
                    pos += REQ_AMOUNT_LEN;

                    //----------开始处理逻辑
                    //更新订单状态
                    OrderVo orderVo = new OrderVo();
                    orderVo.setAcnum(acnum);
                    orderVo.setOrderId(Long.valueOf(orderId));
                    orderVo.setAmount(amount);

                    //更新状态结果
                    /*
                    typedef  struct{
                        unsigned char length;				 //一个字节
                        unsigned char head;				 	 //0xA2
                        unsigned char content[?];            //加密后内容 版本内容(3) + 检验结果（1）+ 机器订单ID（8）+ 注资金额（8）
                        unsigned char check;				 //校验位
                        unsigned char tail;					 //0xD0
                    }__attribute__((packed))T_InjectionAck, *PT_InjectionAck;*/
                    boolean changeRes = false;
                    try {
                        log.info("机器返回注资结果： 更新订单状态 res = " + chargeRes.equals("1"));
                        orderService.updateMachineInjectionStatus(orderVo, chargeRes.equals("1"));
                        changeRes = true;
                    } catch (Exception e) {
                        changeRes = false;
                    }

                    //把临时密钥从redis中删除
                    tempKeyUtils.deleteTempKey(acnum + getCID(ctx));

                    //----------开始返回
                    //返回内容的原始数据
                    String responseData = versionContent + chargeRes + String.format("%08d", Long.valueOf(orderId)) + String.format("%08d", Long.valueOf(amount));
                    //返回内容的加密数据
                    String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
                    return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
                default:
                    throw new Exception("机器返回注资结果：版本不存在");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("机器返回注资结果：解析出错" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
