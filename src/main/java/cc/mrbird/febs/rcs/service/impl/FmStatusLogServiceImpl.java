package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.FmStatusLog;
import cc.mrbird.febs.rcs.mapper.FmStatusLogMapper;
import cc.mrbird.febs.rcs.service.IFmStatusLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 机器状态变更表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:44:22
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FmStatusLogServiceImpl extends ServiceImpl<FmStatusLogMapper, FmStatusLog> implements IFmStatusLogService {

    private final FmStatusLogMapper fmStatusLogMapper;

    @Override
    public IPage<FmStatusLog> findFmStatusLogs(QueryRequest request, FmStatusLog fmStatusLog) {
        LambdaQueryWrapper<FmStatusLog> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<FmStatusLog> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<FmStatusLog> findFmStatusLogs(FmStatusLog fmStatusLog) {
	    LambdaQueryWrapper<FmStatusLog> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFmStatusLog(FmStatusLog fmStatusLog) {
        this.save(fmStatusLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFmStatusLog(FmStatusLog fmStatusLog) {
        this.saveOrUpdate(fmStatusLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFmStatusLog(FmStatusLog fmStatusLog) {
        LambdaQueryWrapper<FmStatusLog> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
