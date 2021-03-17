package cc.mrbird.febs.common.netty.protocol.kit;

import cc.mrbird.febs.common.service.RedisService;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TempKeyUtils {

    @Autowired
    RedisService redisService;


    /**
     * 添加临时密钥
     * 把临时密钥保存到redis中，如果redis中有了表头号，则直接替换
     * @param key
     * @param tempKey
     */
    public synchronized void addTempKey(ChannelHandlerContext ctx, String tempKey){
        redisService.set(createKeyName(ctx), tempKey);
        //6分钟超时
//        redisService.expire(createKeyName(acnum), 360L);
    }

    /**
     * 获取临时密钥
     * @param key
     * @return
     */
    public synchronized String getTempKey(ChannelHandlerContext ctx) throws Exception{
        return (String) redisService.get(createKeyName(ctx));
    }

    /**
     * 删除临时密钥
     * @param key
     */
    public synchronized void deleteTempKey(ChannelHandlerContext ctx){
        redisService.del(createKeyName(ctx));
    }


    private String createKeyName(ChannelHandlerContext ctx){
        String cid = "_null";
        if(ctx != null){
            cid = "_" + ctx.channel().id().toString();
        }
        return "temp:AesKey:" + cid;
    }


}
