package cc.mrbird.febs.notice.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 消息提示表 Controller
 *
 * @author mrbird
 * @date 2020-06-11 15:30:26
 */
@Slf4j
@RequestMapping(FebsConstant.VIEW_PREFIX + "notice")
@Controller("noticeView")
@ApiIgnore
public class ViewController extends BaseController {

    @GetMapping("notice")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_notice_view", isApi = false)
    public String noticeIndex(){
        return FebsUtil.view("notice/notice");
    }

}
