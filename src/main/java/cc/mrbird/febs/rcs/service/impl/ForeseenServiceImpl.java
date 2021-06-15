package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.mapper.ForeseenMapper;
import cc.mrbird.febs.rcs.service.IForeseenService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 预算订单 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:44:51
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ForeseenServiceImpl extends ServiceImpl<ForeseenMapper, Foreseen> implements IForeseenService {

    @Autowired
    ForeseenMapper foreseenMapper;

    @Override
    public IPage<Foreseen> findForeseens(QueryRequest request, Foreseen foreseen) {
        LambdaQueryWrapper<Foreseen> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Foreseen> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Foreseen> findForeseens(Foreseen foreseen) {
	    LambdaQueryWrapper<Foreseen> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createForeseen(Foreseen foreseen) {
        this.save(foreseen);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForeseen(Foreseen foreseen) {
        this.saveOrUpdate(foreseen);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteForeseen(Foreseen foreseen) {
        LambdaQueryWrapper<Foreseen> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
