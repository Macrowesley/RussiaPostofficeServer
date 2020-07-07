package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.service.IOrderService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

/**
 * 查询是否有数据包
 */
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

    //服务器返回加密数据长度(动态)
    private int res_encrty_content_len = 0;

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
     * 解析并返回结果流
     *
     * @param bytes
     * @param ctx
     * @return
     */
    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        //读取加密内容  bytes - type - check - tail
        //解析协议
        /*typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length;				//0x09
            unsigned char type;					//0xA1
            unsigned char acnum[6];             //机器表头号
            unsigned char content[?];           //加密后内容: 版本内容（长度3）
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))QueryData,*QueryData;*/
        try {
            int pos = TYPE_LEN;
            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //加密内容
            String enctryptContent = BaseTypeUtils.byteToString(bytes, pos, bytes.length - TYPE_LEN - REQ_ACNUM_LEN - CHECK_LEN - END_LEN, BaseTypeUtils.UTF8);

            //获取临时密钥
            String tempKey = tempKeyUtils.getTempKey(acnum);

            //解密后内容
            String dectryptContent = AESUtils.decrypt(enctryptContent, tempKey);

            //版本号
            String versionContent = dectryptContent.substring(0, VERSION_LEN);
            pos = VERSION_LEN;


            int version = Integer.valueOf(versionContent);
            switch (version) {
                case 1:
                /*typedef  struct{
                    unsigned char length;				 //一个字节
                    unsigned char head;				 	 //0xA1
                    unsigned char content[?];            //加密后内容 版本内容（3）+ 结果（1） + 机器订单ID（8）+ 注资金额（8）
                    unsigned char check;				 //校验位
                    unsigned char tail;					 //0xD0
                }__attribute__((packed))T_InjectionAck, *PT_InjectionAck;*/

                    log.info("查询是否有数据包：解析加密内容，version={}, acnum={}", versionContent, acnum);

                    log.info("表头号为：" + acnum);
                    Order order = orderService.machineRequestData(acnum);

                    //是否有结果
                    String res = "0";
                    //机器订单ID
                    long orderId = 0;
                    //注资金额 分
                    long finalAmount = 0;
                    if (order != null) {
                        res = "1";
                        //订单金额 元
                        String amount = order.getAmount();
                        orderId = order.getOrderId();
                        finalAmount = MoneyUtils.changeY2F(Double.valueOf(amount));
                    }
                    //返回内容的原始数据
                    String responseData = versionContent + res + String.format("%08d", orderId) + String.format("%08", finalAmount);
                    //返回内容的加密数据
                    String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
                    return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
                default:
                    throw new Exception("版本不存在");
            }
        } catch (Exception e) {
            log.error("解析出错" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
