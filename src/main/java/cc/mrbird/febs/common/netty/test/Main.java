package cc.mrbird.febs.common.netty.test;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
        int corePoolSize = 100;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize);
        executor.setQueueCapacity(2000);
        executor.setKeepAliveSeconds(3600);
        executor.setThreadNamePrefix("netty-test-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        int clientCount = 1000;
        clientCount = 1000;
        int pos = 0;
        System.out.println("开始循环");
        for (int i = 0; i < clientCount; i++) {
//            executor.submit(new ClientRunnable(i+1));
            new Thread(new ClientRunnable(i+1)).start();
            pos++;
        }
        System.out.println("总运行次数：" + pos);

    }
}
