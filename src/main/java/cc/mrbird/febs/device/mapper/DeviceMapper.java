package cc.mrbird.febs.device.mapper;

import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.device.vo.UserDeviceVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备表 Mapper
 *
 *
 * @date 2020-05-27 14:56:25
 */
public interface DeviceMapper extends BaseMapper<Device> {

    <T> IPage<Device> selectListByUserId(Page<T> page, Long userId, @Param("device") Device device);

    List<Device> selectAllListByUserId(Long userId);

    List<Device>  selectSubUserDeviceListExcepBindUserIdByRoleAndParent(@Param("bindUserId") Long bindUserId, @Param("parentUserId") Long parentUserId, @Param("roleType") Long roleType);

    UserDeviceVO findByDeviceIdAndRoleId(@Param("deviceId") Long deviceId, @Param("roleId") Long roleId);
}
