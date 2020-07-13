package cc.mrbird.febs.common.netty.protocol;

import cc.mrbird.febs.common.service.RedisService;
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
     * @param acnum
     * @param tempKey
     */
    public synchronized void addTempKey(String acnum, String tempKey){
        redisService.set(createKeyName(acnum), tempKey);
        //6分钟超时
        redisService.expire(createKeyName(acnum), 360L);
    }

    /**
     * 获取临时密钥
     * @param acnum
     * @return
     */
    public synchronized String getTempKey(String acnum) throws Exception{
        return (String) redisService.get(createKeyName(acnum));
    }

    /**
     * 删除临时密钥
     * @param acnum
     */
    public synchronized void deleteTempKey(String acnum){
        redisService.del(createKeyName(acnum));
    }

    private String createKeyName(String acnum){
        return "acnum:" + acnum;
    }
}
