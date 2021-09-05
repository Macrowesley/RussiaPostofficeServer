package cc.mrbird.febs.common.netty.test;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
        int corePoolSize = 100;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("netty-test-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        int clientCount = 1000;
        clientCount = 1;
        for (int i = 0; i < clientCount; i++) {
            executor.submit(new ClientRunnable(i+1));
        }
    }
}
