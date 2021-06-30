package cc.mrbird.febs.common.netty.protocol.machine.charge;

import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.service.IOrderService;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 查询是否有数据包
 */
@Slf4j
@NoArgsConstructor
@Component
public class QueryProtocol extends MachineToServiceProtocol {
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

    @Autowired
    ChannelMapperManager channelMapperManager;

    public static QueryProtocol queryProtocol;

    @PostConstruct
    public void init(){
        this.queryProtocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return queryProtocol;
    }

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
            unsigned char length[2];				//
            unsigned char type;					//0xA1
            unsigned char  operateID[2];
            unsigned char acnum[6];             //机器表头号
            unsigned char content[?];           //加密后内容: 版本内容（长度3）
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))QueryData,*QueryData;*/
        log.info("【协议】查询是否有数据包");
        /*Thread.sleep(15000);
        log.info("停15s");*/
        try {
            int pos = getBeginPos();
            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            if (!queryProtocol.channelMapperManager.containsKeyAcnum(acnum)){
                log.error("机器返回注资结果：请求不合法");
                throw new Exception("请求不合法");
            }

            //加密内容
            String enctryptContent = BaseTypeUtils.byteToString(bytes, pos, bytes.length - TYPE_LEN - OPERATEID_LEN - REQ_ACNUM_LEN - CHECK_LEN - END_LEN, BaseTypeUtils.UTF8);

            //获取临时密钥
            String tempKey = queryProtocol.tempKeyUtils.getTempKey(ctx);
/*
            log.info("tempKey = " + tempKey + " acnumId = " + (acnum + getCID(ctx)));
            log.info("enctryptContent = " + enctryptContent);
*/
            //解密后内容
            String dectryptContent = AESUtils.decrypt(enctryptContent, tempKey);

            //版本号
            String versionContent = dectryptContent.substring(0, VERSION_LEN);
            pos = VERSION_LEN;


            int version = Integer.valueOf(versionContent);
            switch (version) {
                case 1:
                /*typedef  struct{
                    unsigned char length[2];				 //2个字节
                    unsigned char type;				 	 //0xA1
                    unsigned char  operateID[2];
                    unsigned char content[?];            //加密后内容 版本内容（3）+ 结果（1） + 机器订单ID（8）+ 注资金额（8）
                    unsigned char check;				 //校验位
                    unsigned char tail;					 //0xD0
                }__attribute__((packed))T_InjectionAck, *PT_InjectionAck;*/

                    log.info("查询是否有数据包：解析加密内容，version={}, acnum={}", versionContent, acnum);

                    Order order = queryProtocol.orderService.machineRequestData(acnum);

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
                    String responseData = versionContent + res + String.format("%08d", orderId) + String.format("%08d", finalAmount);
                    //返回内容的加密数据
                    String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
                    log.info("查询是否有数据包：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
                    return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
                default:
                    throw new Exception("查询是否有数据包：版本不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询是否有数据包：解析出错" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
