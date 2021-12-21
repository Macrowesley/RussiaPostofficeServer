package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.service.IMsgService;
import cc.mrbird.febs.rcs.service.INoticeFrontService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MsgServiceImpl implements IMsgService {
    @Autowired
    RedisService redisService;

    @Autowired
    INoticeFrontService noticeFrontService;

    @Value("${msg.ttl}")
    int overtime;

    /**
     * key作用？
     * 1. 能区分具体协议
     * 2. 包含具体printJobId
     * 3. 能通知前端，需要传递printJobId
     */
    @Override
    public String createWebsocketKey(int type, int id) {
        log.info("创建key type = " + type + " id = " + id);
        return "expire-websocket:" + FebsUtil.getCurrentUser().getUserId() + ":" + type + ":" + id;
    }


    @Override
    public void sendMsg(String key, String value) {
        log.info("发送消息 key={}" + key);
        redisService.set(key, value, (long) overtime);
    }

    @Override
    public void receviceMsg(int type, int printJobId, String res) {
        log.info("接收消息：key={}, printJobId={}, res={}", createWebsocketKey(type, printJobId), printJobId, res);
        redisService.del(createWebsocketKey(type, printJobId));

        //通知前端
        noticeFrontService.notice(type, "receive-" + printJobId + "-" + res, FebsUtil.getCurrentUser().getUserId());
    }

    @Override
    public void overtimeMsg(String key) {
        if (key.contains("expire-websocket")) {
            log.info("超时消息key={}", key);
            //拿到过期key ,处理业务逻辑
            String[] split = key.split(":");
            long userId = Long.parseLong(split[1]);
            int type = Integer.parseInt(split[2]);
            String printJobId = split[3];

            //通知前端
            noticeFrontService.notice(type, "overtime-" + printJobId, userId);
        }
    }
}
