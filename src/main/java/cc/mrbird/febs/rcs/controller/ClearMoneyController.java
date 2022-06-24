package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.ui.ClearRecordReq;
import cc.mrbird.febs.system.dto.UserUpdateDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Api(value = "Clear the FM accumulated amount controller")
@Slf4j
@Validated
@RestController()
@RequestMapping("/clearMoney/")
public class ClearMoneyController {

    @ApiOperation("根据FMid查询最新的累计金额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "frankMachineId", value = "frankMachineId", defaultValue = "", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @GetMapping("getMoney/{frankMachineId}")
    public FebsResponse getFmAccumulatedMoney(@PathVariable(required = true) String frankMachineId){
        log.info("收到的fmid = {}", frankMachineId);

        String money = "66.66";
        return new FebsResponse().success().data(money);
    }


    @ApiOperation("保存清除原因")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserUpdateDTO.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @GetMapping("save")
    public FebsResponse save(@RequestBody ClearRecordReq recordReq){
        log.info("收到的recordReq = {}", recordReq.toString());
        return new FebsResponse().success();
    }


    @ApiOperation("保存清除原因")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = ClearRecordReq.class,responseContainer = "List"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @GetMapping("returnList")
    public FebsResponse returnList(@RequestBody QueryRequest request,  @RequestBody ClearRecordReq recordReq){
        log.info("收到的recordReq = {}", recordReq.toString());
        ArrayList<ClearRecordReq> clearRecordReqs = new ArrayList<>();

        return new FebsResponse().success().data(clearRecordReqs);
    }



}
