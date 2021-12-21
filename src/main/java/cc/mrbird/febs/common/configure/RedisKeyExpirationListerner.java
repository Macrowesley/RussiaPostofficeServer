package cc.mrbird.febs.common.configure;

import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.rcs.service.IMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisKeyExpirationListerner extends KeyExpirationEventMessageListener {

    @Autowired
    IMsgService msgService;

    public RedisKeyExpirationListerner(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        msgService.overtimeMsg(message.toString());
    }
}
