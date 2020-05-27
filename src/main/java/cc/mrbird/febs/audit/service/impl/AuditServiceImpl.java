package cc.mrbird.febs.audit.service.impl;

import cc.mrbird.febs.audit.entity.Audit;
import cc.mrbird.febs.audit.mapper.AuditMapper;
import cc.mrbird.febs.audit.service.IAuditService;
import cc.mrbird.febs.common.entity.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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

    @Override
    public IPage<Audit> findAudits(QueryRequest request, Audit audit) {
        LambdaQueryWrapper<Audit> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Audit> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Audit> findAudits(Audit audit) {
	    LambdaQueryWrapper<Audit> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAudit(Audit audit) {
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
}
