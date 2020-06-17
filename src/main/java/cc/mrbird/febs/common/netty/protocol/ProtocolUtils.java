package cc.mrbird.febs.common.netty.protocol;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProtocolUtils {
    private static Logger logger = LoggerFactory.getLogger(ProtocolUtils.class);


    /**
     * 发送给客户端的付款指令
     *
     * @param acnum
     * @param bean
     */
    public void writePayResult() {
/*
//        logger.info("付款回调 连接列表：HeartPortocol.CHANNEL_MAP = " + HeartPortocol.CHANNEL_MAP.toString());
        ChannelHandlerContext ctx = HeartPortocol.CHANNEL_MAP.get(bean.getAcnum());
        if (ctx == null) {
            logger.error("连接不存在，付款成功了，但是无法通知客户端");
        } else {
            //总长度（不包含最前面的长度字节）
            int totalLen = 0;

            //指令类型
            byte[] socketType = {(byte) 0xA2};

            //机器订单ID
            int orderId = bean.getOrderId();
            //int2ByteArraysPos
            byte[] orderIdByteArr = BaseTypeUtils.int2ByteArrayCons(orderId);

            //付款金额
            int amount = bean.getPayMoney();
            byte[] amountByteArr = BaseTypeUtils.int2ByteArrayCons(amount);

            //打印件数
            int num = bean.getPrintNum();
            byte[] numByteArr = BaseTypeUtils.int2ByteArrayCons(num);

            //打印类型
            int printType = bean.getPrintType();
            byte[] printTypeByteArr = new byte[]{BaseTypeUtils.intToByte(printType)};
            //付款状态
            int payStatus = bean.getPayStatus();
            byte[] payStatusByteArr = new byte[]{BaseTypeUtils.intToByte(payStatus)};
            //checkCode
//            int checkCode = Integer.valueOf(bean.getCheckOrder());
//            byte[] checkCodeByteArr = BaseTypeUtils.int2ByteArrayCons(checkCode);
            //校验位
            byte checkSum = 0x00;
            //0xD0
            byte[] end = {(byte) 0xD0};

            totalLen = 1 + orderIdByteArr.length + amountByteArr.length + numByteArr.length + printTypeByteArr.length + payStatusByteArr.length + 2;
            byte[] totalLenByteArr = new byte[]{BaseTypeUtils.intToByte(totalLen)};

            byte[] payResponseBytes = {};

            //指令类型
            payResponseBytes = BaseTypeUtils.byteMerger(payResponseBytes, socketType);
            //机器订单ID
            payResponseBytes = BaseTypeUtils.byteMerger(payResponseBytes, orderIdByteArr);
            //付款金额
            payResponseBytes = BaseTypeUtils.byteMerger(payResponseBytes, amountByteArr);
            //打印件数
            payResponseBytes = BaseTypeUtils.byteMerger(payResponseBytes, numByteArr);
            //打印类型
            payResponseBytes = BaseTypeUtils.byteMerger(payResponseBytes, printTypeByteArr);
            //付款状态
            payResponseBytes = BaseTypeUtils.byteMerger(payResponseBytes, payStatusByteArr);
            //checkCode
//            payResponseBytes = BaseTypeUtils.byteMerger(payResponseBytes, checkCodeByteArr);
            //校验位
            byte[] checkSume = BaseTypeUtils.makeCheckSum(payResponseBytes);
            payResponseBytes = BaseTypeUtils.byteMerger(payResponseBytes, checkSume);
            //0xD0
            payResponseBytes = BaseTypeUtils.byteMerger(payResponseBytes, end);

            //长度
            payResponseBytes = BaseTypeUtils.byteMerger(totalLenByteArr, payResponseBytes);

            //发送给客户端，付款成功了
            ByteBuf buf = Unpooled.buffer(payResponseBytes.length);
            buf.writeBytes(payResponseBytes);
//            ctx.write(buf);
//            ctx.flush();
            ctx.writeAndFlush(buf);
            logger.info("付款 发送给客户端，付款成功了  字节流长度 = " + payResponseBytes.length + "  内容为" + BaseTypeUtils.bytesToHexString(payResponseBytes));
*/

    }

}
