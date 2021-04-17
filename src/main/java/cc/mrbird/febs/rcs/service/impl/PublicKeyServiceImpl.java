package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.mapper.PublicKeyMapper;
import cc.mrbird.febs.rcs.service.IPublicKeyService;
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
 * 公钥表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:23
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PublicKeyServiceImpl extends ServiceImpl<PublicKeyMapper, PublicKey> implements IPublicKeyService {

    private final PublicKeyMapper publicKeyMapper;

    @Override
    public IPage<PublicKey> findPublicKeys(QueryRequest request, PublicKey publicKey) {
        LambdaQueryWrapper<PublicKey> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<PublicKey> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<PublicKey> findPublicKeys(PublicKey publicKey) {
	    LambdaQueryWrapper<PublicKey> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPublicKey(PublicKey publicKey) {
        this.save(publicKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePublicKey(PublicKey publicKey) {
        this.saveOrUpdate(publicKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePublicKey(PublicKey publicKey) {
        LambdaQueryWrapper<PublicKey> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
