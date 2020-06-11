package cc.mrbird.febs.audit.mapper;

import cc.mrbird.febs.audit.entity.Audit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 审核表 Mapper
 */
public interface AuditMapper extends BaseMapper<Audit> {

    /**
     * 系统管理员查看列表
     */
    IPage<Audit> selectBySystemManager(Page<Audit> page, @Param("audit") Audit audit);

    /**
     * 机构管理员查看列表
     */
    IPage<Audit> selectByOrganizationManager(Page<Audit> page, @Param("curUserId") long curUserId, @Param("audit") Audit audit);

    /**
     * 审核管理员查看列表
     */
    IPage<Audit> selectByUserId(Page<Audit> page, @Param("curUserId") long curUserId, @Param("audit") Audit audit);
}
