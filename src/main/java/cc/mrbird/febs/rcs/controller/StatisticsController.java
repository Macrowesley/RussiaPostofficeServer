package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.Statistics;
import cc.mrbird.febs.rcs.service.IStatisticsService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 【待定】 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:39
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class StatisticsController extends BaseController {

    private final IStatisticsService statisticsService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "statistics")
    public String statisticsIndex(){
        return FebsUtil.view("statistics/statistics");
    }

    @GetMapping("statistics")
    @ResponseBody
    @RequiresPermissions("statistics:list")
    public FebsResponse getAllStatisticss(Statistics statistics) {
        return new FebsResponse().success().data(statisticsService.findStatisticss(statistics));
    }

    @GetMapping("statistics/list")
    @ResponseBody
    @RequiresPermissions("statistics:list")
    public FebsResponse statisticsList(QueryRequest request, Statistics statistics) {
        Map<String, Object> dataTable = getDataTable(this.statisticsService.findStatisticss(request, statistics));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Statistics", exceptionMessage = "新增Statistics失败")
    @PostMapping("statistics")
    @ResponseBody
    @RequiresPermissions("statistics:add")
    public FebsResponse addStatistics(@Valid Statistics statistics) {
        this.statisticsService.createStatistics(statistics);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Statistics", exceptionMessage = "删除Statistics失败")
    @GetMapping("statistics/delete")
    @ResponseBody
    @RequiresPermissions("statistics:delete")
    public FebsResponse deleteStatistics(Statistics statistics) {
        this.statisticsService.deleteStatistics(statistics);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Statistics", exceptionMessage = "修改Statistics失败")
    @PostMapping("statistics/update")
    @ResponseBody
    @RequiresPermissions("statistics:update")
    public FebsResponse updateStatistics(Statistics statistics) {
        this.statisticsService.updateStatistics(statistics);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Statistics", exceptionMessage = "导出Excel失败")
    @PostMapping("statistics/excel")
    @ResponseBody
    @RequiresPermissions("statistics:export")
    public void export(QueryRequest queryRequest, Statistics statistics, HttpServletResponse response) {
        List<Statistics> statisticss = this.statisticsService.findStatisticss(queryRequest, statistics).getRecords();
        ExcelKit.$Export(Statistics.class, response).downXlsx(statisticss, false);
    }
}
