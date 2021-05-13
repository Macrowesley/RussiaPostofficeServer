package cc.mrbird.febs.common.netty;

import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperUtils;
import cc.mrbird.febs.common.netty.protocol.machine.ChangeStatusPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.ForeseensCancelPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.ForeseensPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.TransactionsPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.charge.ChargeResProtocol;
import cc.mrbird.febs.common.netty.protocol.machine.charge.QueryProtocol;
import cc.mrbird.febs.common.netty.protocol.machine.heart.HeartPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.result.BalanceResultPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.result.CloseSSHResultPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.result.OpenSSHResultPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.result.UpdateTaxesResultPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.safe.MachineLoginPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.safe.QueryIDPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.safe.QueryTemKeyPortocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 协议处理
 */
@Slf4j
@Component
public class ProtocolService {

    @Autowired
    NettyServerHandler nettyServerHandler;

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
    ForeseensCancelPortocol foreseensCancelPortocol;

    @Autowired
    TransactionsPortocol transactionsPortocol;

    @Autowired
    MachineLoginPortocol machineLoginPortocol;

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

            boolean isNeedLogin = true;
            switch (protocolType) {
                case HeartPortocol.PROTOCOL_TYPE:
                    baseProtocol = heartPortocol;
                    isNeedLogin = false;
                    break;
                case QueryIDPortocol.PROTOCOL_TYPE:
                    baseProtocol = queryIDPortocol;
                    isNeedLogin = false;
                    break;
                case QueryTemKeyPortocol.PROTOCOL_TYPE:
                    baseProtocol = queryTemKeyPortocol;
                    isNeedLogin = false;
                    break;
                case MachineLoginPortocol.PROTOCOL_TYPE:
                    baseProtocol = machineLoginPortocol;
                    isNeedLogin = false;
                    break;
                    //下面所有的操作必须登录了才能执行
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
                case ForeseensCancelPortocol.PROTOCOL_TYPE:
                    baseProtocol = foreseensCancelPortocol;
                    break;
                case TransactionsPortocol.PROTOCOL_TYPE:
                    baseProtocol = transactionsPortocol;
                    break;
                default:
                    return getErrorRes(protocolType);
            }

            try {
                //判断是否通过验证
                if (isNeedLogin){
                    //检查改连接是否保存在缓存中
                    boolean isLogin = ChannelMapperUtils.containsValue(ctx);
                    if (!isLogin){
                        //如果没有登录 删除ctx
                        log.error("ctx = {} 没有通过验证，无法使用，踢掉  当前协议{}", ctx ,  BaseTypeUtils.bytesToHexString(new byte[]{protocolType}));
                        nettyServerHandler.channelInactive(ctx);
                        return getErrorRes(protocolType);
                    }
                }

                return baseProtocol.parseContentAndRspone(data, ctx);
            } catch (Exception e) {
                e.printStackTrace();
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
        return new byte[]{(byte) 0x03, 0x00, protocolType, (byte) 0x00, (byte) 0xD0};
    }
}
