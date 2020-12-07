package cc.mrbird.febs.test.controller;

import cc.mrbird.febs.common.annotation.CheckSign;
import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.RsaSecurityParameter;
import cc.mrbird.febs.test.entity.Persion;
import cc.mrbird.febs.test.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

@RequestMapping("myTest")
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
//    @RsaSecurityParameter
    @ControllerEndpoint(operation = "获取注资分页列表", exceptionMessage = "{order.operation.listError}")
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

    @PostMapping("/signTest1")
    @CheckSign
    public Persion signTest1(@RequestBody Persion info, @RequestParam String teacher, @RequestParam String className) {
        log.info("进行signTest1 Persion = " + info.toString());

        Student student = new Student();
        student.setTeacher(teacher);
        student.setClassName(className);
        log.info("进行signTest1 Student = " + student.toString());
        String content = "内容";
        info.setName(content);
        return  info;
    }

    @GetMapping("/signTest2/{teacher}/{className}")
    @CheckSign
/*
    @ControllerEndpoint(operation = "审核不通过", exceptionMessage = "{audit.operation.updateError}")
    @PostMapping("/signTest2/{teacher}/{className}")
    @RequiresPermissions("audit:update")
*/
    public Student signTest2(@PathVariable String teacher, @PathVariable String className) {
        Student student = new Student();
        student.setTeacher(teacher);
        student.setClassName(className);
        log.info("进行signTest2 Student = " + student.toString());
        String content = "内容";
        student.setClassName("新的班级咯");
        return student;
    }
}
