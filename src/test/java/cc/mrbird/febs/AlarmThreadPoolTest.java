package cc.mrbird.febs;

import cc.mrbird.febs.common.threadpool.AlarmThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlarmThreadPoolTest {
    @Autowired
    AlarmThreadPool alarmThreadPool;


    @Test
    public void test() throws InterruptedException {
//        ExecutorService service = Executors.newFixedThreadPool(6);
//        service.submit(new testRun("AAA"));

//        service.submit(new testRun("AAA"));
//        service.submit(new testRun("AAA"));

        new Thread(new testRun("AAA")).start();
//        myTest("BBB");
//        new Thread(new testRun("BBB")).start();
//        new Thread(new testRun("CCC")).start();
    }

    class testRun implements Runnable {

        private String acnum;

        public testRun(String acnum) {
            this.acnum = acnum;
        }

        @Override
        public void run() {
            myTest(acnum);
        }


    }

    private void myTest(String acnum) {
        try {
            List<String> list = new ArrayList<>();
            int num = 500;
            log.info(acnum + "开始添加");
            for (int i = 0; i < 500; i++) {
//                list.add(acnum + String.format("%05d", i));
                log.info("第"+i+"次循环中");
//                alarmThreadPool.addAlarm(list.get(i));
            }

            /*log.info(acnum + "开始休眠");

            Thread.sleep(4000);

            log.info(acnum + "开始删除");
            for (int i = 0; i < num; i++) {
                alarmThreadPool.deleteAlarm(list.get(i));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
