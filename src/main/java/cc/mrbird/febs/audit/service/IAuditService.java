package cc.mrbird.febs.audit.service;


import cc.mrbird.febs.audit.entity.Audit;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
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
    IPage<Audit> findPageAudits(QueryRequest request, Audit audit);

    /**
     * 查询（所有）
     *
     * @param audit audit
     * @return List<Audit>
     */
    List<Audit> findAuditList(Audit audit);

    /**
     * 根据一个orderId获取列表
     *
     * @return List<Audit>
     */
    List<Audit> findAuditListByOrderId(Long orderId);

    /**
     * 新增
     *
     * @param audit audit
     */
    void createAudit(OrderVo orderVo, String auditType);

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

    /**
     * 订单被注销
     * @param orderId
     */
    void cancelOrder(Long orderId);

    /**
     * 根据orderId获取该订单最新的审核数据
     * @param orderId
     * @return
     */
    Audit getNewestOneByOrderId(Long orderId);

    /**
     * 冻结订单
     * @param orderId
     */
    void freezeOrder(Long orderId);

    /**
     * 解冻订单
     * @param orderId
     */
    void unFreezeOrder(Long orderId);

    /**
     * 审核通过(批量)
     */
    void auditListSuccess(String auditIds);

    /**
     * 审核通过(单个)
     * @param audit
     */
    void auditOneSuccess(Audit audit);

    /**
     * 审核失败(批量)
     */
    void auditListFail(String auditIds);

    /**
     * 审核失败(单个)
     */
    void auditOneFail(Audit audit);
}
