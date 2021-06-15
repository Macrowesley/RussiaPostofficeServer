package cc.mrbird.febs.notice.controller;

import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.websocket.WebSocketServer;
import cc.mrbird.febs.notice.entity.Notice;
import cc.mrbird.febs.notice.service.INoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 消息提示
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("notice")
public class NoticeController extends BaseController {

    @Autowired
    INoticeService noticeService;

    @GetMapping("list")
    @ResponseBody
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_notice_notice")
    public FebsResponse noticeList(QueryRequest request, Notice notice) {
        Map<String, Object> dataTable = getDataTable(this.noticeService.findNotices(request, notice));
        return new FebsResponse().success().data(dataTable);
    }

    @PostMapping("readAllNotice")
    @ResponseBody
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_notice_notice")
    public FebsResponse readAllNotice() {
        noticeService.readAllNotice();
        WebSocketServer.sendInfo(2, MessageUtils.getMessage("notice.clearMessage"), String.valueOf(FebsUtil.getCurrentUser().getUserId()));
        return new FebsResponse().success();
    }

}
