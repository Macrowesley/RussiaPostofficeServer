package cc.mrbird.febs.device.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.device.dto.AddDeviceDTO;
import cc.mrbird.febs.device.dto.UpdateDeviceDTO;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.vo.UserDeviceVO;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.dto.manager.DeviceDTO;
import cc.mrbird.febs.rcs.dto.service.ChangeStatusRequestDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 设备表 Service接口
 *
 *
 * @date 2020-05-27 14:56:25
 */
public interface IDeviceService extends IService<Device> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param device device
     * @return IPage<Device>
     */
    IPage<Device> findDevices(QueryRequest request, Device device);

    /**
     * 新增
     *
     * @param device device
     */
    void createDevice(Device device);

    /**
     * 修改
     *
     * @param device device
     */
    void updateDevice(UpdateDeviceDTO updateDeviceDTO);

    /**
     * 删除
     *
     * @param device device
     */
    void deleteDevice(Device device);

    /**
     * 获取重复表头号信息
     * @param acnumList
     * @return
     */
    Map<String, Object> getRepetitionInfo(String acnumList);

    /**
     * 批量添加设备
     */
    void saveDeviceList(AddDeviceDTO addDeviceDTO);

    /**
     * 根据id查询device
     * @param deviceId
     * @return
     */
    Device findDeviceById(Long deviceId);

    /**
     * 把设备列表绑定到用户上
     * @param deviceIds
     * @param sendUserId
     */
    void bindDevicesToUser(String deviceIds, Long sendUserId);

    /**
     * 从FM中的信息中添加设备
     */
    void addMachineInfo(Device dbDevice, DeviceDTO deviceDTO);

    /**
     * 通过绑定的userId删除所有的关系
     * @param userId
     */
    void deleteDevicesByBindUserId(Long userId);

    /**
     * 根据用户获取设备列表
     * @param bindUserId
     * @return
     */
    List<Device> findAllDeviceListByUserId(Long bindUserId);

    /**
     * 根据用户获取设备id数组
     * @param bindUserId
     * @return
     */
    Long[] findDeviceIdArrByUserId(Long bindUserId);

    /**
     * 获取穿梭框需要的表头号信息
     * @param bindUserId
     * @return
     */
    Map<String, Object> selectDeviceListToTransfer(String bindUserId);

    /**
     * 获取父id下的所有的设备管理者(传进来的设备管理者除外)id绑定的表头号
     * @return
     */
    List<Device> selectSubUserDeviceListExcepBindUserIdByRoleAndParent(Long bindUserId, Long parentUserId, Long roleType);

    /**
     * 查询这个设备绑定了哪个机构管理员
     * @param deviceId
     * @return
     */
    UserDeviceVO findByDeviceIdAndRoleId(Long deviceId, Long roleId);

    Device findDeviceByAcnum(String acnum);

    /**
     * 改变机器状态：开始
     * @param frankMachineId
     * @param changeStatusRequestDTO
     */
    void changeStatusBegin(String frankMachineId, ChangeStatusRequestDTO changeStatusRequestDTO);

    /**
     * 改变机器状态：结束
     * @param deviceDTO
     * @param flowRes
     */
    void changeStatusEnd(DeviceDTO deviceDTO, FlowDetailEnum curFlowDetail, boolean isMachineActive);

    /**
     * 改变device flow 状态
     * @param id
     * @param curFlowDetail
     */
    void changeAuthStatus(Device dbDevice, String frankMachineId, FlowDetailEnum curFlowDetail);
    void changeUnauthStatus(Device dbDevice, String frankMachineId, FlowDetailEnum curFlowDetail);
    void changeLostStatus(Device dbDevice, String frankMachineId, FlowDetailEnum curFlowDetail);
    void changeForeseensStatus(Device dbDevice, FlowDetailEnum curFlowDetail);
    /**
     * 通过frankMachineId得到acnum
     * @param frankMachineId
     * @return
     */
    String getAcnumByFMId(String frankMachineId);

    /**
     * 通过frankMachineId判断是否存在
     * @param frankMachineId
     * @return
     */
    boolean checkExistByFmId(String frankMachineId);

    Device getDeviceByFrankMachineId(String frankMachineId);

    FlowDetailEnum getFlowDetail(String frankMachineId);

    /**
     * 更新所有device的taxIsUpdate 全都改成0
     */
    void updateLastestTaxVersionUpdateStatuts();

    /**
     * 更新device的taxIsUpdate信息和其他信息
     * @param device
     */
    void updateDeviceTaxVersionStatus(Device device);


}
