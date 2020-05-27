package cc.mrbird.febs.audit.service;


import cc.mrbird.febs.audit.entity.Audit;
import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 审核表 Service接口
 *
 *
 * @date 2020-05-27 14:56:09
 */
public interface IAuditService extends IService<Audit> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param audit audit
     * @return IPage<Audit>
     */
    IPage<Audit> findAudits(QueryRequest request, Audit audit);

    /**
     * 查询（所有）
     *
     * @param audit audit
     * @return List<Audit>
     */
    List<Audit> findAudits(Audit audit);

    /**
     * 新增
     *
     * @param audit audit
     */
    void createAudit(Audit audit);

    /**
     * 修改
     *
     * @param audit audit
     */
    void updateAudit(Audit audit);

    /**
     * 删除
     *
     * @param audit audit
     */
    void deleteAudit(Audit audit);
}
