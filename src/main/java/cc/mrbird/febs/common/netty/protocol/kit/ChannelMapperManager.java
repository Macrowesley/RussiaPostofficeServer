package cc.mrbird.febs.common.netty.protocol.kit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 管理机器连接通道
 */
@Slf4j
@Component
public class ChannelMapperManager {
    static int initialCapacity = 300;

    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    public static ConcurrentHashMap<ChannelId, ChannelHandlerContext> All_CHANNEL_MAP = new ConcurrentHashMap<>(initialCapacity);


    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static ConcurrentHashMap<String, ChannelHandlerContext> loginChannelMap = new ConcurrentHashMap<>(initialCapacity);
    private static volatile AtomicInteger channelCount = new AtomicInteger(0);

    @Autowired
    public TempKeyUtils tempKeyUtils;

    @Autowired
    public TempTimeUtils tempTimeUtils;

    public ChannelMapperManager() {
    }


    /**
     * 获取map
     * @return
     */
    public synchronized ConcurrentHashMap<String, ChannelHandlerContext> getLoginChannelMap(){
        return loginChannelMap;
    }

    /**
     * 有新的机器连接
     * @param acnum
     * @param ctx
     */
    public synchronized void addChannel(String acnum, ChannelHandlerContext ctx){
        if (!StringUtils.isEmpty(acnum)) {
            loginChannelMap.put(acnum, ctx);
            channelCount.incrementAndGet();

            ChannelId channelId = ctx.channel().id();
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

            String clientIp = insocket.getAddress().getHostAddress();
            int clientPort = insocket.getPort();
            log.info("长连接：客户端【" + channelId + "】信息 [IP:" + clientIp + "--->PORT:" + clientPort + "]" + " insocket="+insocket.toString());
            log.info("添加机器{}长连接，现在有{}个连接",acnum,getChannleSize());
        }
    }

    /**
     * 根据key删除连接缓存(出现异常，手动移除出了问题的连接)
     * @param acnum
     */
    public synchronized void deleteChannelByKey(String acnum){
        if (!StringUtils.isEmpty(acnum) && loginChannelMap.containsKey(acnum)){
            channelCount.decrementAndGet();
            loginChannelMap.remove(acnum);
            log.info("删除机器{}长连接，现在有{}个连接",acnum,getChannleSize());
        }else{
            log.info("不存在，所以删除机器{}长连接失败", acnum);
        }
    }

    /**
     * 根据value删除连接缓存
     * @param ctx
     */
    public synchronized void deleteChannelByValue(ChannelHandlerContext ctx){
        if (loginChannelMap.containsValue(ctx)){
            channelCount.decrementAndGet();
            for (String key: loginChannelMap.keySet()){
                if (ctx.equals(loginChannelMap.get(key))){
                    loginChannelMap.remove(key);
                    log.info("删除机器{}长连接，现在有{}个连接",key,getChannleSize());
                }
            }
        }
    }

    /**
     * 清空缓存
     */
    public synchronized void clearChannel(){
        loginChannelMap.clear();
    }

    /**
     * 判断是否包含key
     * @param acnum
     * @return
     */
    public synchronized boolean containsKeyAcnum(String acnum){
        return loginChannelMap.containsKey(acnum);
    }

    public synchronized boolean containsValue(ChannelHandlerContext ctx){
        return loginChannelMap.containsValue(ctx);
    }

    /**
     * 根据key获取连接信息
     * @param acnum
     * @return
     */
    public synchronized ChannelHandlerContext getChannelByAcnum(String acnum){
        if (!StringUtils.isEmpty(acnum) && loginChannelMap.containsKey(acnum)){
            return loginChannelMap.get(acnum);
        }
        return null;
    }


    /**
     * 获取在线的机器列表
     *
     * @return
     */
    public synchronized int getChannleSize(){
        return channelCount.get();
    }

    public synchronized void removeCache(String acnum) {
        removeCache(getChannelByAcnum(acnum));
    }
    /**
     * 删除缓存
     * @param ctx
     */
    public synchronized void removeCache(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();

        ChannelId channelId = ctx.channel().id();

        //把临时密钥从redis中删除
        tempKeyUtils.deleteTempKey(ctx);

        tempTimeUtils.deleteTempTime(ctx);

        deleteChannelByValue(ctx);

        //包含此客户端才去删除
        if (All_CHANNEL_MAP.containsKey(channelId)) {

            //删除连接
            All_CHANNEL_MAP.remove(channelId);

            log.info("客户端【" + channelId + "】退出netty服务器[IP:" + clientIp + "--->PORT:" + insocket.getPort() + "]");
            log.info("连接通道数量: " + All_CHANNEL_MAP.size() + " 有效连接数量：" + getChannleSize() );
        }
    }
}
