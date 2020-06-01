package cc.mrbird.febs.device.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.service.IDeviceService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 设备表 Controller
 *
 *
 * @date 2020-05-27 14:56:25
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("device")
public class DeviceController extends BaseController {

    private final IDeviceService deviceService;

    @ControllerEndpoint(operation = "获取列表", exceptionMessage = "获取列表失败")
    @GetMapping("list")
    @RequiresPermissions("device:list")
    public FebsResponse deviceList(QueryRequest request, Device device) {
        log.info("request = " + request.toString());
        Map<String, Object> dataTable = getDataTable(this.deviceService.findDevices(request, device));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Device", exceptionMessage = "新增Device失败")
    @PostMapping("add")
    @RequiresPermissions("device:add")
    public FebsResponse addDevice(Device device, String acnumList) {
        if (acnumList.length() > 8 && !acnumList.contains(",")){
            throw new FebsException("数据格式不对");
        }
        log.info("device={}, acnumList={}", device.toString(), acnumList);
        this.deviceService.saveDeviceList(device, acnumList);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Device", exceptionMessage = "删除Device失败")
    @GetMapping("delete")
    @RequiresPermissions("device:delete")
    public FebsResponse deleteDevice(Device device) {
        this.deviceService.deleteDevice(device);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Device", exceptionMessage = "修改Device失败")
    @PostMapping("update")
    @RequiresPermissions("device:update")
    public FebsResponse updateDevice(Device device) {
        this.deviceService.updateDevice(device);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出Excel", exceptionMessage = "导出Excel失败")
    @GetMapping("excel")
    @RequiresPermissions("device:export")
    public void export(QueryRequest queryRequest, Device device, HttpServletResponse response) {
        List<Device> devices = this.deviceService.findDevices(queryRequest, device).getRecords();
        ExcelKit.$Export(Device.class, response).downXlsx(devices, false);
    }

    @ControllerEndpoint(operation = "检查表头号是否存在", exceptionMessage = "检查表头号是否存在失败")
    @PostMapping("checkIsExist/{acnumList}")
    public FebsResponse checkIsExist(@NotBlank @PathVariable("acnumList") String acnumList) {
        if (acnumList.length() > 8 && !acnumList.contains(",")){
            throw new FebsException("数据格式不对");
        }
        Map<String, Object> res =  deviceService.getRepetitionInfo(acnumList);
        return new FebsResponse().success().data(res);
    }
}
