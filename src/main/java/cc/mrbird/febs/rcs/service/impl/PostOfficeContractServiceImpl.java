package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.PostOfficeContract;
import cc.mrbird.febs.rcs.mapper.PostOfficeContractMapper;
import cc.mrbird.febs.rcs.service.IPostOfficeContractService;
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
 * 邮局-合同关系表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:46:07
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PostOfficeContractServiceImpl extends ServiceImpl<PostOfficeContractMapper, PostOfficeContract> implements IPostOfficeContractService {

    private final PostOfficeContractMapper postOfficeContractMapper;

    @Override
    public IPage<PostOfficeContract> findPostOfficeContracts(QueryRequest request, PostOfficeContract postOfficeContract) {
        LambdaQueryWrapper<PostOfficeContract> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<PostOfficeContract> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<PostOfficeContract> findPostOfficeContracts(PostOfficeContract postOfficeContract) {
	    LambdaQueryWrapper<PostOfficeContract> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPostOfficeContract(PostOfficeContract postOfficeContract) {
        this.save(postOfficeContract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePostOfficeContract(PostOfficeContract postOfficeContract) {
        this.saveOrUpdate(postOfficeContract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePostOfficeContract(PostOfficeContract postOfficeContract) {
        LambdaQueryWrapper<PostOfficeContract> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
