package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Statistics;
import cc.mrbird.febs.rcs.mapper.StatisticsMapper;
import cc.mrbird.febs.rcs.service.IStatisticsService;
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
 * 【待定】 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:39
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Statistics> implements IStatisticsService {

    private final StatisticsMapper statisticsMapper;

    @Override
    public IPage<Statistics> findStatisticss(QueryRequest request, Statistics statistics) {
        LambdaQueryWrapper<Statistics> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Statistics> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Statistics> findStatisticss(Statistics statistics) {
	    LambdaQueryWrapper<Statistics> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createStatistics(Statistics statistics) {
        this.save(statistics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatistics(Statistics statistics) {
        this.saveOrUpdate(statistics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStatistics(Statistics statistics) {
        LambdaQueryWrapper<Statistics> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
