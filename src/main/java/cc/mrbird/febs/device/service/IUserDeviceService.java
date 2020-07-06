package cc.mrbird.febs.device.service;

import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.entity.UserDevice;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户设备关联表 Service接口
 *
 * @author mrbird
 * @date 2020-05-29 14:38:03
 */
public interface IUserDeviceService extends IService<UserDevice> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param userDevice userDevice
     * @return IPage<UserDevice>
     */
    IPage<UserDevice> findUserDevices(QueryRequest request, UserDevice userDevice);

    /**
     * 查询（所有）
     *
     * @param userDevice userDevice
     * @return List<UserDevice>
     */
    List<UserDevice> findUserDevices(UserDevice userDevice);

    /**
     * 新增
     *
     * @param userDevice userDevice
     */
    void createUserDevice(UserDevice userDevice);

    /**
     * 修改
     *
     * @param userDevice userDevice
     */
    void updateUserDevice(UserDevice userDevice);

    /**
     * 删除
     *
     * @param userDevice userDevice
     */
    void deleteUserDevice(UserDevice userDevice);

    UserDevice findOneUserDevice(UserDevice userDevice);
}
