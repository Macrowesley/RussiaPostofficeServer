package cc.mrbird.febs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("cc.mrbird.febs.*.mapper")
public class PostmartOfficeApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PostmartOfficeApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
