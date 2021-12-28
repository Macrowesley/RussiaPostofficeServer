package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.service.IMsgService;
import cc.mrbird.febs.rcs.service.INoticeFrontService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
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

    @Override
    public String createWebsocketKey(int type, int printJobId, int userId) {
        log.info("创建key type = " + type + " printJobId = " + printJobId + " userId = " + userId + " key = " + "expire-websocket:" + userId + ":" + type + ":" + printJobId);
        return "expire-websocket:" + userId + ":" + type + ":" + printJobId;
    }

    @Override
    public void sendMsg(int type, int printJobId, String value) {
        if (FebsUtil.isLogin()) {
            log.info("发送消息");
            redisService.set(createWebsocketKey(type, printJobId, FebsUtil.getCurrentUser().getUserId().intValue()), value, (long) overtime);
        }
    }

    @Override
    public void receviceMsg(int type, int printJobId, String res, int userId) {
        log.info("接收消息： printJobId={}, res={}", printJobId, res);
        redisService.del(createWebsocketKey(type, printJobId,userId));

        if (FebsUtil.isLogin()) {
            //通知前端
            noticeFrontService.notice(type, "receive-" + printJobId + "-" + res, userId);
        }
    }

    @Override
    public void overtimeMsg(String key) {
        if (key.contains("expire-websocket")) {
            log.info("超时消息key={}", key);
            //todo 拿到过期key ,处理业务逻辑
            String[] split = key.split(":");
            int userId = Integer.parseInt(split[1]);
            int type = Integer.parseInt(split[2]);
            String printJobId = split[3];

            if (FebsUtil.isLogin()) {
                //通知前端
                noticeFrontService.notice(type, "overtime-" + printJobId, userId);
            }
        }
    }
}
