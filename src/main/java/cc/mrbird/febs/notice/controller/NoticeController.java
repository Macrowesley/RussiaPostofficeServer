package cc.mrbird.febs.notice.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.websocket.WebSocketServer;
import cc.mrbird.febs.notice.entity.Notice;
import cc.mrbird.febs.notice.service.INoticeService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
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

    private final INoticeService noticeService;

    @GetMapping("list")
    @ResponseBody
    public FebsResponse noticeList(QueryRequest request, Notice notice) {
        Map<String, Object> dataTable = getDataTable(this.noticeService.findNotices(request, notice));
        return new FebsResponse().success().data(dataTable);
    }

    @PostMapping("readAllNotice")
    @ResponseBody
    public FebsResponse readAllNotice() {
        noticeService.readAllNotice();
        WebSocketServer.sendInfo(2, MessageUtils.getMessage("notice.clearMessage"), String.valueOf(FebsUtil.getCurrentUser().getUserId()));
        return new FebsResponse().success();
    }

}
