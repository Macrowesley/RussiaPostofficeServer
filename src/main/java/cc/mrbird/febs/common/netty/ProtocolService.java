package cc.mrbird.febs.common.netty;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.machine.*;
import cc.mrbird.febs.common.netty.protocol.machine.charge.ChargeResProtocol;
import cc.mrbird.febs.common.netty.protocol.machine.result.BalanceResultPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.result.UpdateTaxesResultPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.safe.QueryIDPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.charge.QueryProtocol;
import cc.mrbird.febs.common.netty.protocol.machine.safe.QueryTemKeyPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.heart.HeartPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.result.CloseSSHResultPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.result.OpenSSHResultPortocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AUTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 协议处理
 */
@Slf4j
@Component
public class ProtocolService {

    @Autowired
    HeartPortocol heartPortocol;

    @Autowired
    QueryProtocol queryProtocol;

    @Autowired
    ChargeResProtocol chargeResProtocol;

    @Autowired
    QueryIDPortocol queryIDPortocol;

    @Autowired
    QueryTemKeyPortocol queryTemKeyPortocol;

    @Autowired
    OpenSSHResultPortocol openSSHResultPortocol;

    @Autowired
    CloseSSHResultPortocol closeSSHResultPortocol;

    @Autowired
    BalanceResultPortocol balanceResultPortocol;

    @Autowired
    UpdateTaxesResultPortocol updateTaxesResultPortocol;

    @Autowired
    ChangeStatusPortocol changeStatusPortocol;

    @Autowired
    ForeseensPortocol foreseensPortocol;

    @Autowired
    CancelJobPortocol cancelJobPortocol;

    @Autowired
    TransactionsPortocol transactionsPortocol;

    //出问题了返回该结果
    private byte[] emptyResBytes = new byte[]{(byte) 0xA0, (byte) 0xFF, (byte) 0xD0};

    @Autowired
    private ApplicationContext applicationContext;

//    @Async(FebsConstant.NETTY_ASYNC_POOL)
    public void parseAndResponse(SocketData msg, ChannelHandlerContext ctx) {
        if (msg == null) {
            log.error("socketData为null，不可用");
            return;
        }

        try {
            log.info("线程" + Thread.currentThread().getName() + "中，客户端【" + ctx.channel().id() + "】发送数据给客户端");
            wrieteToCustomer(ctx, parseContentAndRspone(msg.getContent(), ctx));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 服务端给客户端发送消息
     */
    public void wrieteToCustomer(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
        if (ctx == null) {
            log.info("通道不存在");
            return;
        }

        if (bytes == null) {
            log.info("服务端响应空的消息");
            return;
        }

        ByteBuf buf = Unpooled.buffer(bytes.length);
        buf.writeBytes(bytes);
        ctx.write(buf);
        ctx.flush();
    }


    /**
     * 解析数据内容
     *
     * @param data
     * @return
     */
    public synchronized byte[] parseContentAndRspone(byte[] data, ChannelHandlerContext ctx) {
        //验证校验位
        if (BaseTypeUtils.checkChkSum(data, data.length - 2)) {
            //解析类型
            byte protocolType = MachineToServiceProtocol.parseType(data);

            MachineToServiceProtocol baseProtocol = null;
            switch (protocolType) {
                case HeartPortocol.PROTOCOL_TYPE:
                    baseProtocol = heartPortocol;
                    break;
                case QueryIDPortocol.PROTOCOL_TYPE:
                    baseProtocol = queryIDPortocol;
                    break;
                case QueryTemKeyPortocol.PROTOCOL_TYPE:
                    baseProtocol = queryTemKeyPortocol;
                    break;
                case QueryProtocol.PROTOCOL_TYPE:
                    baseProtocol = queryProtocol;
                    break;
                case ChargeResProtocol.PROTOCOL_TYPE:
                    baseProtocol = chargeResProtocol;
                    break;
                case OpenSSHResultPortocol.PROTOCOL_TYPE:
                    baseProtocol = openSSHResultPortocol;
                    break;
                case CloseSSHResultPortocol.PROTOCOL_TYPE:
                    baseProtocol = closeSSHResultPortocol;
                    break;
                case BalanceResultPortocol.PROTOCOL_TYPE:
                    baseProtocol = balanceResultPortocol;
                    break;
                case UpdateTaxesResultPortocol.PROTOCOL_TYPE:
                    baseProtocol = updateTaxesResultPortocol;
                    break;
                case ChangeStatusPortocol.PROTOCOL_TYPE:
                    baseProtocol = changeStatusPortocol;
                    break;
                case ForeseensPortocol.PROTOCOL_TYPE:
                    baseProtocol = foreseensPortocol;
                    break;
                case CancelJobPortocol.PROTOCOL_TYPE:
                    baseProtocol = cancelJobPortocol;
                    break;
                case TransactionsPortocol.PROTOCOL_TYPE:
                    baseProtocol = transactionsPortocol;
                    break;
                default:
                    return getErrorRes(protocolType);
            }

            try {
                return baseProtocol.parseContentAndRspone(data, ctx);
            } catch (Exception e) {
                log.error("返回结果出错：" + e.getMessage());
                return getErrorRes(protocolType);
            }
        } else {
            log.error("校验位验证错误");
            return emptyResBytes;
        }
    }

    /**
     * 特殊异常
     * @param protocolType
     * @return
     */
    private byte[] getErrorRes(byte protocolType) {
        return new byte[]{(byte) 0x03, protocolType, (byte) 0x00, (byte) 0xD0};
    }
}
