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
import cc.mrbird.febs.system.service.IUserRoleService;
import cc.mrbird.febs.system.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
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

    @ControllerEndpoint(operation = "获取页面列表", exceptionMessage = "{device.operation.listError}")
    @GetMapping("list")
    @RequiresPermissions("device:list")
    public FebsResponse devicePageList(QueryRequest request, Device device) {
        log.info("request = " + request.toString());
        Map<String, Object> dataTable = getDataTable(this.deviceService.findDevices(request, device));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "获取列表", exceptionMessage = "{device.operation.listError}")
    @GetMapping("allList/{bindUserId}")
    public FebsResponse allList(@NotBlank(message = "{required}") @PathVariable String bindUserId) {
        Map<String, Object> data =  deviceService.selectDeviceListToTransfer(bindUserId);
        return new FebsResponse().success().data(data);
    }

    @ControllerEndpoint(operation = "分配设备", exceptionMessage = "{device.operation.deviceError}")
    @PostMapping("sendDevice/{deviceIds}/{bindUserId}")
//    @RequiresPermissions("device:add")
    public FebsResponse sendDevice(@NotBlank(message = "{required}") @PathVariable String deviceIds,
                                   @NotBlank(message = "{required}") @PathVariable String bindUserId) {
        log.info("获取的分配设备id = " + deviceIds + " 绑定的userId = " + bindUserId);
        deviceService.bindDevicesToUser(deviceIds, Long.valueOf(bindUserId));
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Device", exceptionMessage = "{device.operation.addDeviceError}")
    @PostMapping("add")
    @RequiresPermissions("device:add")
    public FebsResponse addDevice(Device device, String bindUserId, String acnumList) {
        log.info("新增device device= {}， bindUserId = {}, acnumList = {}" , device.toString(), bindUserId, acnumList);
        if (acnumList.length() > 8 && !acnumList.contains(",")){
            throw new FebsException("数据格式不对");
        }
        this.deviceService.saveDeviceList(device, acnumList, bindUserId);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Device", exceptionMessage = "{device.operation.delDeviceError}")
    @GetMapping("delete")
    @RequiresPermissions("device:delete")
    public FebsResponse deleteDevice(Device device) {
        this.deviceService.deleteDevice(device);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Device", exceptionMessage = "{device.operation.editDeviceError}")
    @PostMapping("update/{userdeviceId}")
    @RequiresPermissions("device:update")
    public FebsResponse updateDevice(Device device, String bindUserId, @NotBlank @PathVariable String userdeviceId) {
        this.deviceService.updateDevice(device, bindUserId, userdeviceId);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出Excel", exceptionMessage = "{device.operation.exportError}")
    @GetMapping("excel")
    @RequiresPermissions("device:export")
    public void export(QueryRequest queryRequest, Device device, HttpServletResponse response) {
        List<Device> devices = this.deviceService.findDevices(queryRequest, device).getRecords();
        ExcelKit.$Export(Device.class, response).downXlsx(devices, false);
    }

    @ControllerEndpoint(operation = "检查表头号是否存在", exceptionMessage = "{device.operation.checkAcnumError}")
    @PostMapping("checkIsExist/{acnumList}")
    public FebsResponse checkIsExist(@NotBlank @PathVariable("acnumList") String acnumList) {
        if (acnumList.length() > 8 && !acnumList.contains(",")){
            throw new FebsException("数据格式不对");
        }
        Map<String, Object> res =  deviceService.getRepetitionInfo(acnumList);
        return new FebsResponse().success().data(res);
    }
}
