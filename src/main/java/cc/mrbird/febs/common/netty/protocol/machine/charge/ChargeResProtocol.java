package cc.mrbird.febs.common.netty.protocol.machine.charge;

import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 机器返回注资结果
 */
@Slf4j
@NoArgsConstructor
@Component
public class ChargeResProtocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xA2;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //注资结果长度
    private static final int REQ_CHARGE_RES_LEN = 1;

    //机器订单ID长度
    private static final int REQ_ORDERID_LEN = 8;

    //注资金额长度
    private static final int REQ_AMOUNT_LEN = 8;

    @Autowired
    ChannelMapperManager channelMapperManager;

    @Autowired
    IOrderService orderService;

    public static ChargeResProtocol chargeResProtocol;

    @PostConstruct
    public void init(){
        chargeResProtocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return chargeResProtocol;
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
        try {
            /*typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[4];				//
                unsigned char type;					//0xA2
                unsigned char  operateID[2];
                unsigned char acnum[6];             //机器表头号
                unsigned char content[?];           //加密后内容 版本内容(3) + 注资结果（1）+ 机器订单ID（8）+ 注资金额（8）
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))Result,*Result;*/
            int pos = getBeginPos();
            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            if (!chargeResProtocol.channelMapperManager.containsKeyAcnum(acnum)){
                log.error("机器返回注资结果：请求不合法");
                throw new Exception("请求不合法");
            }
            pos += REQ_ACNUM_LEN;

            //加密内容
            String enctryptContent = BaseTypeUtils.byteToString(bytes, pos, bytes.length - TYPE_LEN - OPERATEID_LEN - REQ_ACNUM_LEN - CHECK_LEN - END_LEN, BaseTypeUtils.UTF8);

            //获取临时密钥
            String tempKey = chargeResProtocol.tempKeyUtils.getTempKey(ctx);

            //解密后内容
            String dectryptContent = AESUtils.decrypt(enctryptContent, tempKey);

            String versionContent = dectryptContent.substring(0, VERSION_LEN);
            pos = VERSION_LEN;


            log.info("【协议】机器返回注资结果： 解析加密内容，version={}, acnum={}", versionContent, acnum);

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
                        unsigned char length[4];				 //2个字节
                        unsigned char type;				 	 //0xA2
                        unsigned char  operateID[2];
                        unsigned char content[?];            //加密后内容 版本内容(3) + 检验结果（1）+ 机器订单ID（8）+ 注资金额（8）
                        unsigned char check;				 //校验位
                        unsigned char tail;					 //0xD0
                    }__attribute__((packed))T_InjectionAck, *PT_InjectionAck;*/
                    boolean changeRes = false;
                    try {
                        log.info("机器{}返回注资结果： 更新订单状态 res = {}" ,acnum, chargeRes.equals("1"));
                        chargeResProtocol.orderService.updateMachineInjectionStatus(orderVo, chargeRes.equals("1"));
                        changeRes = true;
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        changeRes = false;
                        throw new Exception(e.getMessage());
                    }



                    //----------开始返回
                    //返回内容的原始数据
                    String responseData = versionContent + chargeRes + String.format("%08d", Long.valueOf(orderId)) + String.format("%08d", Long.valueOf(amount));
                    //返回内容的加密数据
                    String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
                    log.info("解析并返回结果流");
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
