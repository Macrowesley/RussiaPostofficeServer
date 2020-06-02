package cc.mrbird.febs.device.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.device.entity.Device;
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
    void updateDevice(Device device);

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
     * @param device
     * @param acnumList
     */
    void saveDeviceList(Device device, String acnumList);

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
     * 通过绑定的userId删除所有的关系
     * @param userId
     */
    void deleteDevicesByBindUserId(Long userId);

    /**
     * 根据用户获取设备列表
     * @param bindUserId
     * @return
     */
    List<Device> findDeviceListByUserId(Long bindUserId);

    /**
     * 根据用户获取设备id数组
     * @param bindUserId
     * @return
     */
    Long[] findDeviceIdArrByUserId(Long bindUserId);
}
