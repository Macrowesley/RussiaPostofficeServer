package cc.mrbird.febs.common.netty.test;

import cc.mrbird.febs.system.entity.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        int corePoolSize = 200;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize);
        executor.setQueueCapacity(2000);
        executor.setKeepAliveSeconds(3600);
        executor.setThreadNamePrefix("netty-test-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        int clientCount = 1000;
        clientCount = 500;
        clientCount = 1;
        System.out.println("开始循环");
        for (int i = 0; i < clientCount; i++) {
            //executor.submit(new TestChangeStatusByMacro(i+1));
           new Thread(new TestChangeStatusByMacro(i+1)).start();
        }
    }
}
