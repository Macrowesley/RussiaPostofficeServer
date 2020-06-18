package cc.mrbird.febs.common.threadpool;

import ch.qos.logback.core.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * 定时报警线程池
 */
@Slf4j
@Service
public class AlarmThreadPool {
    private static ConcurrentHashMap<Long, ScheduledFuture<String>> map;
    private final ScheduledExecutorService service;

    public AlarmThreadPool() {
        log.info("初始化AlarmThreadPool");
        service = Executors.newScheduledThreadPool(10);
        map = new ConcurrentHashMap<>();
    }

    /**
     * 添加警告
     * @param acnum
     */
    public synchronized void addAlarm(Long orderId){
        try {
            deleteAlarm(orderId);
            ScheduledFuture<String> future = (ScheduledFuture<String>) service.schedule(new AlarmRunnable(orderId), 5, TimeUnit.SECONDS);
            map.put(orderId, future);
            log.info("添加警告 " + orderId + " map.size = " + map.size());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 删除警告
     * @param acnum
     */
    public synchronized void deleteAlarm(Long orderId){
        if (map.containsKey(orderId)){
            ScheduledFuture<String> future = map.get(orderId);
            future.cancel(true);
            map.remove(orderId);
        }
        log.info("删除警告 " + orderId + " map.size = " + map.size());
    }

    public static ConcurrentHashMap<Long, ScheduledFuture<String>> getMap() {
        return map;
    }
}
