package cc.mrbird.febs.common.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("common")
public class CommonController {
    @Autowired
    private TokenUtil tokenUtil;

    @GetMapping("getTime")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_get_time")
    public FebsResponse getTime() {
        return new FebsResponse().success().data(System.currentTimeMillis());
    }


    /**
     * 获取 Token 接口
     *
     * @return Token 串
     */
    @GetMapping("token")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_get_time")
    public FebsResponse getToken() {
//        log.info("获取token");
        String userInfo = FebsUtil.getCurrentUser().getUserId().toString();
        return new FebsResponse().success().data(tokenUtil.generateToken(userInfo));
    }
}
