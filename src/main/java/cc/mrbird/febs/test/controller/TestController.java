package cc.mrbird.febs.test.controller;

import cc.mrbird.febs.common.annotation.RsaSecurityParameter;
import cc.mrbird.febs.test.entity.Persion;
import cc.mrbird.febs.test.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("test")
@RestController
@Slf4j
public class TestController {
    /**
     * 跳转rsa页面
     *
     * @return
     */

    @GetMapping("/rsa")
    public String goRsa() {
        return "rsa";
    }

    /**
     * RSA测试
     *
     * @return object
     */
    @PostMapping("/rsaTest")
    @RsaSecurityParameter
    public Persion rsaTest(@RequestBody Persion info, Student student) {
        log.info("进行RSA测试1 Persion = " + info.toString());
        log.info("进行RSA测试1 Student = " + student.toString());
        String content = "内容";
        info.setName(content);
        return  info;
    }


    @PostMapping("/rsaTest2")
    public Persion rsaTest2(@RequestBody Persion info) {
        log.info("进行RSA测试2 " + info.toString());
        String content = "内容2";
        info.setName(content);
        return  info;
    }
}
