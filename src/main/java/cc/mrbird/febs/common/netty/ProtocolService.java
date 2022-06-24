package cc.mrbird.febs.common.netty;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.kit.ChannelMapperManager;
import cc.mrbird.febs.common.netty.protocol.machine.*;
import cc.mrbird.febs.common.netty.protocol.machine.heart.HeartPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.publickey.QueryPrivateKeylPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.publickey.UpdatePrivateKeyResultPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.result.*;
import cc.mrbird.febs.common.netty.protocol.machine.safe.MachineLoginPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.safe.QueryIDPortocol;
import cc.mrbird.febs.common.netty.protocol.machine.safe.QueryTemKeyPortocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 协议处理
 */
@Slf4j
@Component
public class ProtocolService {
    @Autowired
    ChannelMapperManager channelMapperManager;

    @Autowired
    HeartPortocol heartPortocol;


    @Autowired
    QueryIDPortocol queryIDPortocol;

    @Autowired
    QueryTemKeyPortocol queryTemKeyPortocol;

    @Autowired
    OpenSSHResultPortocol openSSHResultPortocol;

    @Autowired
    CloseSSHResultPortocol closeSSHResultPortocol;


    @Autowired
    UpdatePrivateKeyResultPortocol updatePrivateKeyResultPortocol;

    @Autowired
    QueryPrivateKeylPortocol queryPrivateKeylPortocol;

    @Autowired
    ChangeStatusPortocol changeStatusPortocol;

    @Autowired
    ForeseensPortocol foreseensPortocol;

   /* @Autowired
    ForeseensCancelPortocol foreseensCancelPortocol;*/

    @Autowired
    TransactionsPortocol transactionsPortocol;

    @Autowired
    MachineLoginPortocol machineLoginPortocol;

    @Autowired
    CheckServicePortocol checkServicePortocol;

    @Autowired
    TransactionMsgPortocol transactionMsgPortocol;

    @Autowired
    ClickPrintResultPortocol clickPrintResultPortocol;

    @Autowired
    CancelPrintResultPortocol cancelPrintResultPortocol;

    @Autowired
    UpdateRemoteFileResultPortocol updateRemoteFileResultPortocol;

    @Autowired
    ClearMoneyResultPortocol clearMoneyResultPortocol;

    @Autowired
    AdDownloadResultPortocol adDownloadResultPortocol;

    @Autowired
    @Qualifier(value = FebsConstant.NETTY_ASYNC_POOL)
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    //出问题了返回该结果
    public byte[] emptyResBytes = new byte[]{(byte) 0xA0, (byte) 0xFF, (byte) 0xD0};


 /*   @Async(FebsConstant.NETTY_ASYNC_POOL)
    public void parseAndResponse(SocketData msg, ChannelHandlerContext ctx) {
        byte[] data = msg.getContent();
        try {
            //验证校验位 测试情况除外
            if (!BaseTypeUtils.checkChkSum(data, data.length - 2) && !FebsConstant.IS_TEST_NETTY) {
                log.error("校验位验证错误");
                wrieteToCustomer(ctx, emptyResBytes);
            }
            //解析类型
            byte protocolType = MachineToServiceProtocol.parseType(data);

            if (protocolType == HeartPortocol.PROTOCOL_TYPE){
                byte[] operateIdArr = new byte[]{data[1],data[2]};
                heartPortocol.setOperateIdArr(operateIdArr);
                wrieteToCustomer(ctx, heartPortocol.parseContentAndRspone(data, ctx));
            }else{
                parseContentAndWrite(data,protocolType, ctx);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
            data = null;
        }
    }*/

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
    @Async(FebsConstant.NETTY_ASYNC_POOL)
    public void parseContentAndWrite(byte[] data, byte protocolType,  ChannelHandlerContext ctx) throws Exception {
        //解析操作id字节数组
        byte[] operateIdArr = new byte[]{data[1],data[2]};
        MachineToServiceProtocol baseProtocol = null;

        boolean isNeedLogin = true;
        Boolean isNeedAsync = true;
        boolean isNewObject = false;
        switch (protocolType) {
            /*case HeartPortocol.PROTOCOL_TYPE:
                baseProtocol = heartPortocol;
                isNeedLogin = false;
                isNeedAsync = false;
                break;*/
            case QueryIDPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new QueryIDPortocol() : queryIDPortocol;
                isNeedLogin = false;
                break;
            case QueryTemKeyPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new QueryTemKeyPortocol() : queryTemKeyPortocol;
                isNeedLogin = false;
                break;
            case MachineLoginPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new MachineLoginPortocol() : machineLoginPortocol;
                isNeedLogin = false;
                break;
            //下面所有的操作必须登录了才能执行
            case CheckServicePortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new CheckServicePortocol() : checkServicePortocol;
                break;
            /*
            case BalanceResultPortocol.PROTOCOL_TYPE:
                baseProtocol = new BalanceResultPortocol();
                break;*/
            case OpenSSHResultPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new OpenSSHResultPortocol() : openSSHResultPortocol;
                break;
            case CloseSSHResultPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new CloseSSHResultPortocol() : closeSSHResultPortocol;
                break;

            case UpdatePrivateKeyResultPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new UpdatePrivateKeyResultPortocol() : updatePrivateKeyResultPortocol;
                break;
            case QueryPrivateKeylPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new QueryPrivateKeylPortocol() : queryPrivateKeylPortocol;
                break;
            case ChangeStatusPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new ChangeStatusPortocol() : changeStatusPortocol;
                break;
            case ForeseensPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new ForeseensPortocol() : foreseensPortocol;
                //临时测试
                if (FebsConstant.IS_TEST_NETTY){
                    isNeedLogin = false;
                }
                break;
            /*case ForeseensCancelPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new ForeseensCancelPortocol() : foreseensCancelPortocol;
                break;*/
            case TransactionsPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new TransactionsPortocol() : transactionsPortocol;
                break;
            case TransactionMsgPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new TransactionMsgPortocol() : transactionMsgPortocol;
                break;
            case ClickPrintResultPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new ClickPrintResultPortocol() : clickPrintResultPortocol;
                break;
            case CancelPrintResultPortocol.PROTOCOL_TYPE:
                baseProtocol = isNewObject ? new CancelPrintResultPortocol() : cancelPrintResultPortocol;
                break;
            case UpdateRemoteFileResultPortocol.PROTOCOL_TYPE:
                baseProtocol = updateRemoteFileResultPortocol;
                break;
            case ClearMoneyResultPortocol.PROTOCOL_TYPE:
                baseProtocol = clearMoneyResultPortocol;
                break;
            case AdDownloadResultPortocol.PROTOCOL_TYPE:
                baseProtocol = adDownloadResultPortocol;
                break;
            default:
                log.error("protocolType 格式不对： " + BaseTypeUtils.bytesToHexString(new byte[]{protocolType}));
                wrieteToCustomer(ctx, getErrorRes(protocolType, operateIdArr));
                return;
        }

        try {
            //测试情况，就不验证了，直接放到里面
            if (FebsConstant.IS_TEST_NETTY){
                String acnum = BaseTypeUtils.byteToString(data, baseProtocol.getBeginPos(), 6, BaseTypeUtils.UTF8);
                //保存到缓存
                if (!channelMapperManager.containsKeyAcnum(acnum)) {
                    channelMapperManager.addChannel(acnum, ctx);
                }
                isNeedLogin = false;
            }
            //判断是否通过验证 测试情况例外
            if (isNeedLogin) {
                //检查改连接是否保存在缓存中
                boolean isLogin = channelMapperManager.containsValue(ctx);
                if (!isLogin) {
                    //如果没有登录 删除ctx
                    log.error("ctx = {} 没有通过验证，无法使用，踢掉  当前协议{}", ctx, BaseTypeUtils.bytesToHexString(new byte[]{protocolType}));
                    channelMapperManager.removeCache(ctx);
                    ctx.disconnect().sync();
                    wrieteToCustomer(ctx, getErrorRes(protocolType, operateIdArr));
                }
            }
            /*if (isNeedAsync){
                MachineToServiceProtocol asyncProtocol = baseProtocol;
                threadPoolTaskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            log.info("【处理协议 开始】");
                            long t1 = System.currentTimeMillis();
                            asyncProtocol.setOperateIdArr(operateIdArr);
                            wrieteToCustomer(ctx, asyncProtocol.parseContentAndRspone(data, ctx));
                            log.info("【处理协议{} 结束 耗时：{}】",BaseTypeUtils.bytesToHexString(new byte[]{protocolType}), (System.currentTimeMillis() - t1));
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("返回结果出错：" + e.getMessage());
                        }
                    }
                });
            }else{
                baseProtocol.setOperateIdArr(operateIdArr);
                wrieteToCustomer(ctx, baseProtocol.parseContentAndRspone(data, ctx));
            }*/
            log.info("【处理协议{} 开始】", baseProtocol.getProtocolName());
            long t1 = System.currentTimeMillis();
            baseProtocol.setOperateIdArr(operateIdArr);
            wrieteToCustomer(ctx, baseProtocol.parseContentAndRspone(data, ctx));
            log.info("【处理协议{} 结束 耗时：{}】", baseProtocol.getProtocolName(), (System.currentTimeMillis() - t1));

        } catch (Exception e) {
            e.printStackTrace();
            log.error("返回结果出错：" + e.getMessage());
            wrieteToCustomer(ctx, getErrorRes(protocolType, operateIdArr));
        } finally {

        }

    }

    /**
     * 特殊异常
     *
     * @param protocolType
     * @return
     */
    private byte[] getErrorRes(byte protocolType, byte[] operateIdArr) {
        return new byte[]{(byte) 0x03, 0x00, protocolType, operateIdArr[0],operateIdArr[1], (byte) 0x00, (byte) 0xD0};
    }
}
