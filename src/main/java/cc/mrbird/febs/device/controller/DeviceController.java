package cc.mrbird.febs.device.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
public class DeviceController extends BaseController {

    private final IDeviceService deviceService;
    
    @GetMapping("device")
    @RequiresPermissions("device:list")
    public FebsResponse getAllDevices(Device device) {
        return new FebsResponse().success().data(deviceService.findDevices(device));
    }

    @GetMapping("device/list")
    @RequiresPermissions("device:list")
    public FebsResponse deviceList(QueryRequest request, Device device) {
        Map<String, Object> dataTable = getDataTable(this.deviceService.findDevices(request, device));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Device", exceptionMessage = "新增Device失败")
    @PostMapping("device")
    @RequiresPermissions("device:add")
    public FebsResponse addDevice(@Valid Device device) {
        this.deviceService.createDevice(device);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Device", exceptionMessage = "删除Device失败")
    @GetMapping("device/delete")
    @RequiresPermissions("device:delete")
    public FebsResponse deleteDevice(Device device) {
        this.deviceService.deleteDevice(device);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Device", exceptionMessage = "修改Device失败")
    @PostMapping("device/update")
    @RequiresPermissions("device:update")
    public FebsResponse updateDevice(Device device) {
        this.deviceService.updateDevice(device);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Device", exceptionMessage = "导出Excel失败")
    @PostMapping("device/excel")
    @RequiresPermissions("device:export")
    public void export(QueryRequest queryRequest, Device device, HttpServletResponse response) {
        List<Device> devices = this.deviceService.findDevices(queryRequest, device).getRecords();
        ExcelKit.$Export(Device.class, response).downXlsx(devices, false);
    }
}
