package cc.mrbird.febs.test.controller;

import cc.mrbird.febs.system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/test/rest")
@RestController
@Slf4j
@ApiIgnore
public class RestTestController {

    @GetMapping("/getWithMap1")
    public String sayHello(String name, String age) {
        return "name =  " + name + " age = " + age + " !";
    }

    @GetMapping("/getWithMap2/{name}/{age}")
    public String getWithMap(@PathVariable String name, @PathVariable String age) {
        return "name =  " + name + " age = " + age + " !";
    }

    @PostMapping("/postInPath")
    public String postInPath(String name, String age) {
        return "name =  " + name + " age = " + age + " !";
    }

    @PostMapping("/postWithJson")
    public User hello(@RequestBody User user) {
        return user;
    }

    @PostMapping("/postWithJsonAndPath/{userId}/{age}")
    public User postWithJsonAndPath(@RequestBody User user, @PathVariable int userId, @PathVariable int age) {
        log.info("userId={},age={}", userId, age);
        return user;
    }

    @PutMapping("/putInPathReturnNull/{userId}/{age}")
    public void putInPathReturnNull(@PathVariable int userId, @PathVariable int age, User user) {
        log.info("userId={},age={}", userId, age);
        log.info("user = {}", user);
    }

    /**
     * 有@RequestBody说明接收的是json
     * 没有@RequestBody接收的是key-value
     *
     * @param User
     */
    @PutMapping("/putWithJsonReturnNull")
    public void putWithJsonReturnNull(@RequestBody User user) {
        log.info("user = {}", user);
    }

    @PutMapping("/putWithObjectReturn")
    public String putWithObjectReturn(@RequestBody User user, HttpServletRequest request) {
        log.info("user = {}", user);
        return "cookie = " + request.getHeader("cookie") + " 收到的user=" + user.toString();
    }

    @DeleteMapping("/deleteValue1/{id}")
    @ResponseBody
    public String deleteValue1(@PathVariable Integer id) {
        log.info("" + id);
        return "收到的id="+id;
    }

    @DeleteMapping("/deleteValue2")
    @ResponseBody
    public String deleteValue2(Integer id) {
        log.info("" + id);
        return "收到的id="+id;
    }

    @PostMapping("/postwithHeader")
    public String postwithHeader(HttpServletRequest request) {
        log.info(request.getHeader("cookie"));
        return request.getHeader("cookie");
    }

}
