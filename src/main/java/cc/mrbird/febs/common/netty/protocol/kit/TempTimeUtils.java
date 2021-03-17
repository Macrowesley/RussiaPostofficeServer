package cc.mrbird.febs.common.netty.protocol.kit;

import cc.mrbird.febs.common.service.RedisService;
import cn.hutool.core.date.DateBetween;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class TempTimeUtils {

    @Autowired
    RedisService redisService;


    /**
     * 添加临时时间戳
     * 把临时时间戳保存到redis中，如果redis中有了表头号，则直接替换
     * @param key
     * @param tempKey
     */
    public synchronized void addTempTime(ChannelHandlerContext ctx, long timestamp){
        redisService.set(createKeyName(ctx), timestamp);
    }

    /**
     * 获取临时时间戳
     * @param key
     * @return
     */
    public synchronized Long getTempTime(ChannelHandlerContext ctx) throws Exception{
        return (Long) redisService.get(createKeyName(ctx));
    }

    /**
     * 删除临时时间戳
     * @param key
     */
    public synchronized void deleteTempTime(ChannelHandlerContext ctx){
        redisService.del(createKeyName(ctx));
    }

    /**
     * 判断这个时间戳是否有效
     * @return
     */
    public boolean isValidTime(ChannelHandlerContext ctx, long machineIime) {
        try {
            //缓存中是否有这个时间戳
            Long oldTime = getTempTime(ctx);
            if (oldTime == null){
                return false;
            }

            if (oldTime != machineIime){
                return false;
            }
            //时间是否超时60s
            if (System.currentTimeMillis() - oldTime.longValue() > 1000 * 60){
                return false;
            }

            //验证通过，删除缓存
            deleteTempTime(ctx);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    private String createKeyName(ChannelHandlerContext ctx){
        String cid = "_null";
        if(ctx != null){
            cid = "_" + ctx.channel().id().toString();
        }
        return "temp:timeKey:" + cid;
    }
    
}
