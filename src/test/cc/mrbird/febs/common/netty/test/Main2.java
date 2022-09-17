package netty.test;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.text.NumberFormat;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Main2 {
    public static volatile AtomicInteger channelCount = new AtomicInteger(0);
    public static void main(String[] args) {
        int corePoolSize = 300;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize);
        executor.setQueueCapacity(2000);
        executor.setKeepAliveSeconds(3600);
        executor.setThreadNamePrefix("netty-test-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        int clientCount = 1001;
        //clientCount = 6;
        clientCount = 2;
        //clientCount = 1000;
        System.out.println("开始循环");
        for (int i = 1; i < clientCount; i++) {

            //生成不同的frankMachineId
            String FMHead = "PM";
            //得到一个NumberFormat的实例
            NumberFormat nf = NumberFormat.getInstance();
            //设置是否使用分组
            nf.setGroupingUsed(false);
            //设置最大整数位数
            nf.setMaximumIntegerDigits(6);
            //设置最小整数位数
            nf.setMinimumIntegerDigits(6);
            String frankMachineId = FMHead + nf.format(i + 1);
            System.out.println(frankMachineId);

            //创建1000条订单/装备10000个acnum数据
            String acnumHead = "S0";
            //得到一个NumberFormat的实例
            NumberFormat nf1 = NumberFormat.getInstance();
            //设置是否使用分组
            nf1.setGroupingUsed(false);
            //设置最大整数位数
            nf1.setMaximumIntegerDigits(4);
            //设置最小整数位数
            nf1.setMinimumIntegerDigits(4);
            String acnum = acnumHead + nf1.format(i + 1);
            System.out.println(acnum);


            //new Thread(new ClientRunnable(acnum,frankMachineId)).start();
            executor.submit(new TestChangeStatusByMacro(i+1));
            //new Thread(new TestChangeStatusByMacro(i+1)).start();
            //executor.submit(new TestQueryTemKeyByMacro(i+1));
        }
    }
}
