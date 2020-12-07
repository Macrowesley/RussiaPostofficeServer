package cc.mrbird.febs.device.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.constant.LimitConstant;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.device.dto.AddDeviceDTO;
import cc.mrbird.febs.device.dto.CheckIsExistDTO;
import cc.mrbird.febs.device.dto.SendDeviceDTO;
import cc.mrbird.febs.device.dto.UpdateDeviceDTO;
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
import javax.validation.constraints.NotNull;
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
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_device_device")
    public FebsResponse devicePageList(QueryRequest request, Device device) {
        log.info("request = " + request.toString());
        Map<String, Object> dataTable = getDataTable(this.deviceService.findDevices(request, device));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "获取列表", exceptionMessage = "{device.operation.listError}")
    @GetMapping("allList/{bindUserId}")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_device_device")
    public FebsResponse allList(@NotBlank(message = "{required}") @PathVariable String bindUserId) {
        Map<String, Object> data =  deviceService.selectDeviceListToTransfer(bindUserId);
        return new FebsResponse().success().data(data);
    }

    @ControllerEndpoint(operation = "分配设备", exceptionMessage = "{device.operation.deviceError}")
    @PostMapping("sendDevice")
//    @RequiresPermissions("device:add")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_device_device")
    public FebsResponse sendDevice(@Validated SendDeviceDTO deviceDTO) {
        log.info("获取的分配设备id = " + deviceDTO.getDeviceIds() + " 绑定的userId = " + deviceDTO.getBindUserId());
        deviceService.bindDevicesToUser(deviceDTO.getDeviceIds(), deviceDTO.getBindUserId());
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "新增Device", exceptionMessage = "{device.operation.addDeviceError}")
    @PostMapping("add")
    @RequiresPermissions("device:add")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_device_device")
    public FebsResponse addDevice(@Validated @NotNull AddDeviceDTO deviceDTO) {
        log.info("新增device deviceDTO= {}" , deviceDTO.toString());
        if (deviceDTO.getAcnumList().length() < 6 && !deviceDTO.getAcnumList().contains(",")){
            throw new FebsException(MessageUtils.getMessage("IncorrectDataFormat"));
        }
        this.deviceService.saveDeviceList(deviceDTO);
        return new FebsResponse().success();
    }

    /*@ControllerEndpoint(operation = "删除Device", exceptionMessage = "{device.operation.delDeviceError}")
    @GetMapping("delete")
    @RequiresPermissions("device:delete")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_device_device")
    public FebsResponse deleteDevice(Device device) {
        this.deviceService.deleteDevice(device);
        return new FebsResponse().success();
    }*/

    @ControllerEndpoint(operation = "修改Device", exceptionMessage = "{device.operation.editDeviceError}")
    @PostMapping("update")
    @RequiresPermissions("device:update")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_device_device")
    public FebsResponse updateDevice(@Validated @NotNull UpdateDeviceDTO updateDeviceDTO) {
        this.deviceService.updateDevice(updateDeviceDTO);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "导出Excel", exceptionMessage = "{device.operation.exportError}")
    @GetMapping("excel")
    @RequiresPermissions("device:export")
    @Limit(period = LimitConstant.Strict.period, count = LimitConstant.Strict.count, prefix = "limit_device_device")
    public void export(QueryRequest queryRequest, Device device, HttpServletResponse response) {
        List<Device> devices = this.deviceService.findDevices(queryRequest, device).getRecords();
        ExcelKit.$Export(Device.class, response).downXlsx(devices, false);
    }

    @ControllerEndpoint(operation = "检查表头号是否存在", exceptionMessage = "{device.operation.checkAcnumError}")
    @PostMapping("checkIsExist")
    @Limit(period = LimitConstant.Loose.period, count = LimitConstant.Loose.count, prefix = "limit_device_device")
    public FebsResponse checkIsExist(@Validated CheckIsExistDTO checkIsExistDTO) {
        if (checkIsExistDTO.getAcnumList().length() < 6 && !checkIsExistDTO.getAcnumList().contains(",")){
            throw new FebsException(MessageUtils.getMessage("IncorrectDataFormat"));
        }
        Map<String, Object> res =  deviceService.getRepetitionInfo(checkIsExistDTO.getAcnumList());
        return new FebsResponse().success().data(res);
    }
}
