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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 消息提示
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("notice")
@Api(description = "List for notices and read notices ")
public class NoticeController extends BaseController {

    @Autowired
    INoticeService noticeService;

    @GetMapping("list")
    @ResponseBody
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_notice_notice")
    @ApiOperation("List for notices")
    @ApiIgnore
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Notice.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    public FebsResponse noticeList(QueryRequest request, @RequestBody Notice notice) {
        Map<String, Object> dataTable = getDataTable(this.noticeService.findNotices(request, notice));
        return new FebsResponse().success().data(dataTable);
    }

    @PostMapping("readAllNotice")
    @ResponseBody
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_notice_notice")
    @ApiOperation("Read all notices")
    @ApiIgnore
    public FebsResponse readAllNotice() {
        noticeService.readAllNotice();
        WebSocketServer.sendInfo(2, MessageUtils.getMessage("notice.clearMessage"), String.valueOf(FebsUtil.getCurrentUser().getUserId()));
        return new FebsResponse().success();
    }

}
