package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface UserMapper extends BaseMapper<User> {

    /**
     *  查找用户
     *
     * @return 用户
     */
    User findByName(@Param("user") User user);

    /**
     * 查找用户详细信息
     *
     * @param page 分页对象
     * @param user 用户对象，用于传递查询条件
     * @return Ipage
     */
    <T> IPage<User> findUserDetailPage(Page<T> page, @Param("user") User user);

    List<User> findUserDetailPage(@Param("user") User user);

    long countUserDetail(@Param("user") User user);

    /**
     * 查找用户详细信息
     *
     * @param user 用户对象，用于传递查询条件
     * @return List<User>
     */
    List<User> findUserDetail(@Param("user") User user);

    List<Map<String, Object>> findAuditListByDeviceId(@Param("deviceId") Long deviceId);

    /**
     * 根据用户角色搜索用户信息
     * @param roleId
     * @return
     */
    List<User> findUserByRoleId(@Param("roleId") String roleId);
}
