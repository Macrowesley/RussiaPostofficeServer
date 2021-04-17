package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Frank;
import cc.mrbird.febs.rcs.mapper.FrankMapper;
import cc.mrbird.febs.rcs.service.IFrankService;
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
 * 【待定】这个表的父表是哪个？Statistics还是Transaction Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:32
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FrankServiceImpl extends ServiceImpl<FrankMapper, Frank> implements IFrankService {

    private final FrankMapper frankMapper;

    @Override
    public IPage<Frank> findFranks(QueryRequest request, Frank frank) {
        LambdaQueryWrapper<Frank> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Frank> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Frank> findFranks(Frank frank) {
	    LambdaQueryWrapper<Frank> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFrank(Frank frank) {
        this.save(frank);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFrank(Frank frank) {
        this.saveOrUpdate(frank);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFrank(Frank frank) {
        LambdaQueryWrapper<Frank> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
