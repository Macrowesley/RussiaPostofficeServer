package cc.mrbird.febs.common.netty.protocol.kit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 管理机器连接通道
 */
@Slf4j
public class ChannelMapperUtils {
    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static ConcurrentHashMap<String, ChannelHandlerContext> channelMap = new ConcurrentHashMap<>(100);
    private static volatile AtomicInteger channelCount = new AtomicInteger(0);

    public ChannelMapperUtils() {
    }

    /**
     * 获取map
     * @return
     */
    public static ConcurrentHashMap<String, ChannelHandlerContext> getChannelMap(){
        return channelMap;
    }

    /**
     * 有新的机器连接
     * @param acnum
     * @param ctx
     */
    public static void addChannel(String acnum, ChannelHandlerContext ctx){
        if (!StringUtils.isEmpty(acnum)) {
            channelMap.put(acnum, ctx);
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
    public static void deleteChannelByKey(String acnum){
        if (!StringUtils.isEmpty(acnum) && channelMap.containsKey(acnum)){
            channelCount.decrementAndGet();
            channelMap.remove(acnum);
            log.info("删除机器{}长连接，现在有{}个连接",acnum,getChannleSize());
        }else{
            log.info("不存在，所以删除机器{}长连接失败", acnum);
        }
    }

    /**
     * 根据value删除连接缓存
     * @param ctx
     */
    public static void deleteChannelByValue(ChannelHandlerContext ctx){
        if (channelMap.containsValue(ctx)){
            channelCount.decrementAndGet();
            for (String key: channelMap.keySet()){
                if (ctx.equals(channelMap.get(key))){
                    channelMap.remove(key);
                    log.info("删除机器{}长连接，现在有{}个连接",key,getChannleSize());
                }
            }
        }
    }

    /**
     * 清空缓存
     */
    public static void clearChannel(){
        channelMap.clear();
    }

    /**
     * 判断是否包含key
     * @param acnum
     * @return
     */
    public static boolean containsKey(String acnum){
        return channelMap.containsKey(acnum);
    }

    public static boolean containsValue(ChannelHandlerContext ctx){
        return channelMap.containsValue(ctx);
    }

    /**
     * 根据key获取连接信息
     * @param acnum
     * @return
     */
    public static ChannelHandlerContext getChannelByAcnum(String acnum){
        if (!StringUtils.isEmpty(acnum) && channelMap.containsKey(acnum)){
            return channelMap.get(acnum);
        }
        return null;
    }


    /**
     * 获取在线的机器列表
     *
     * @return
     */
    public static int getChannleSize(){
        return channelCount.get();
    }

}
