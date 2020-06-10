package cc.mrbird.febs.audit.service.impl;

import cc.mrbird.febs.audit.entity.Audit;
import cc.mrbird.febs.audit.mapper.AuditMapper;
import cc.mrbird.febs.audit.service.IAuditService;
import cc.mrbird.febs.common.entity.AuditType;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.enums.AuditStatusEnum;
import cc.mrbird.febs.common.enums.OrderStatusEnum;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.order.entity.Order;
import cc.mrbird.febs.order.entity.OrderVo;
import cc.mrbird.febs.order.service.IOrderService;
import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 审核表 Service实现
 *
 *
 * @date 2020-05-27 14:56:09
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AuditServiceImpl extends ServiceImpl<AuditMapper, Audit> implements IAuditService {

    private final AuditMapper auditMapper;

    @Autowired
    IOrderService orderService;

    @Override
    public IPage<Audit> findPageAudits(QueryRequest request, Audit audit) {
        LambdaQueryWrapper<Audit> queryWrapper = new LambdaQueryWrapper<>();

        Page<Audit> page = new Page<>(request.getPageNum(), request.getPageSize());
        if (StringUtils.isNotBlank(audit.getAuditType())){
            queryWrapper.eq(Audit::getAuditType, audit.getAuditType());
        }

        /*if (StringUtils.isNotBlank(audit.getAcnum())){
            queryWrapper.eq(Audit::getAcnum, audit.getAcnum());
        }*/

        if (StringUtils.isNotBlank(audit.getStatus())){
            queryWrapper.eq(Audit::getStatus, audit.getStatus());
        }

        if (StringUtils.isNotBlank(audit.getOrderNumber())){
            queryWrapper.eq(Audit::getOrderNumber, audit.getOrderNumber());
        }

        //todo 需要重新写sql
        String roleId = FebsUtil.getCurrentUser().getRoleId();
        switch (roleId){
            case RoleType.systemManager:
                break;
            case RoleType.organizationManager:
                break;
            default:

                break;
        }


        queryWrapper.orderByDesc(Audit::getAuditId);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Audit> findAuditList(Audit audit) {
	    LambdaQueryWrapper<Audit> queryWrapper = new LambdaQueryWrapper<>();
		return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据一个orderId获取列表
     *
     * @param orderId
     * @return List<Audit>
     */
    @Override
    public List<Audit> findAuditListByOrderId(Long orderId) {
        LambdaQueryWrapper<Audit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Audit::getOrderId, orderId);
        queryWrapper.orderByDesc(Audit::getAuditId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAudit(OrderVo orderVo, String auditType) {
        Audit audit = new Audit();
        audit.setOrderId(orderVo.getOrderId());
        audit.setUserId(orderVo.getAuditUserId());
        audit.setOrderNumber(orderVo.getOrderNumber());
        audit.setDeviceId(orderVo.getDeviceId());
        audit.setAmount(orderVo.getAmount());
        audit.setAuditType(AuditType.injection);
        audit.setFUserId(orderVo.getApplyUserId());
        audit.setStatus(AuditStatusEnum.notBegin.getStatus());
        audit.setCreateTime(new Date());
        audit.setSubmitInfo(orderVo.getSubmitInfo());
        this.save(audit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAudit(Audit audit) {
        this.saveOrUpdate(audit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAudit(Audit audit) {
        LambdaQueryWrapper<Audit> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        Audit audit = getNewestOneByOrderId(orderId);
        audit.setOldStatus(audit.getStatus());
        audit.setStatus(AuditStatusEnum.orderRepeal.getStatus());
        updateAudit(audit);
    }

    /**
     * 根据orderId获取该订单最新的审核数据
     *
     * @param orderId
     * @return
     */
    @Override
    public Audit getNewestOneByOrderId(Long orderId) {
        LambdaQueryWrapper<Audit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Audit::getOrderId, orderId);
        queryWrapper.last("limit 1");
        queryWrapper.orderByDesc(Audit::getAuditId);
        List<Audit> list = this.baseMapper.selectList(queryWrapper);
        if (list.size() == 1){
            return list.get(0);
        }
        throw new FebsException("审核表中没有订单id为"+orderId+"的订单信息");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeOrder(Long orderId) {
        Audit audit = getNewestOneByOrderId(orderId);
        audit.setOldStatus(audit.getStatus());
        audit.setStatus(AuditStatusEnum.orderFreezeing.getStatus());
        updateAudit(audit);
    }

    /**
     * 解冻订单
     *
     * @param orderId
     */
    @Override
    public void unFreezeOrder(Long orderId) {
        Audit audit = getNewestOneByOrderId(orderId);
        audit.setStatus(audit.getOldStatus());
        audit.setOldStatus(null);
        updateAudit(audit);
    }

    /**
     * 审核通过(批量)
     *
     * @param auditIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditListSuccess(String auditIds) {
        List<String> auditIdList = Arrays.asList(auditIds.split(StringPool.COMMA));
        auditIdList.stream().forEach(auditId ->{
            //更新审核状态
            Audit bean =  baseMapper.selectById(auditId);
            auditOneSuccess(bean);
        });
    }

    /**
     * 审核通过(单个)
     *
     * @param audit
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditOneSuccess(Audit audit) {
        //验证
        checkCanUpdate(audit);

        //更新审核状态
        audit.setOldStatus(null);
        audit.setStatus(AuditStatusEnum.success.getStatus());
        updateAudit(audit);

        //更新订单状态
        Order order = orderService.findOrderByOrderId(audit.getOrderId());
        order.setOrderStatus(OrderStatusEnum.auditPass.getStatus());
        orderService.updateOrder(order);
    }

    /**
     * 审核失败(批量)
     *
     * @param auditIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditListFail(String auditIds) {
        List<String> auditIdList = Arrays.asList(auditIds.split(StringPool.COMMA));
        auditIdList.stream().forEach(auditId ->{
            //更新审核状态
            Audit bean = baseMapper.selectById(auditId);
            auditOneFail(bean);
        });
    }

    /**
     * 审核失败(单个)
     *
     * @param audit
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditOneFail(Audit audit) {
        //验证
        checkCanUpdate(audit);

        //更新状态
        audit.setOldStatus(null);
        audit.setStatus(AuditStatusEnum.notPass.getStatus());
        updateAudit(audit);

        //更新订单状态
        Order order = orderService.findOrderByOrderId(audit.getOrderId());
        switch (audit.getAuditType()){
            case AuditType.injection:
                order.setOrderStatus(OrderStatusEnum.auditNotPass.getStatus());
                break;
            case AuditType.closedCycle:
                order.setOrderStatus(OrderStatusEnum.orderCloseApplyNotPass.getStatus());
                break;
            default:
                throw new FebsException("类型不正确，无法操作");
        }

        orderService.updateOrder(order);
    }

    /**
     * 验证是否可以审核
     * @param audit
     */
    private void checkCanUpdate(Audit audit) {
        if (audit.getStatus().equals(AuditStatusEnum.orderFreezeing) || audit.getStatus().equals(AuditStatusEnum.orderFreezeing)){
            throw new FebsException("该订单已被冻结或者注销，无法审核");
        }
    }


}
