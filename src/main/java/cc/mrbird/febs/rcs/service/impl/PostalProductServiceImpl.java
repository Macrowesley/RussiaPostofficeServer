package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.PostalProduct;
import cc.mrbird.febs.rcs.mapper.PostalProductMapper;
import cc.mrbird.febs.rcs.service.IPostalProductService;
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
 * 邮政产品表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:46:18
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PostalProductServiceImpl extends ServiceImpl<PostalProductMapper, PostalProduct> implements IPostalProductService {

    @Autowired
    PostalProductMapper postalProductMapper;

    @Override
    public IPage<PostalProduct> findPostalProducts(QueryRequest request, PostalProduct postalProduct) {
        LambdaQueryWrapper<PostalProduct> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<PostalProduct> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<PostalProduct> findPostalProducts(PostalProduct postalProduct) {
	    LambdaQueryWrapper<PostalProduct> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPostalProduct(PostalProduct postalProduct) {
        this.save(postalProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePostalProduct(PostalProduct postalProduct) {
        this.saveOrUpdate(postalProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePostalProduct(PostalProduct postalProduct) {
        LambdaQueryWrapper<PostalProduct> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
