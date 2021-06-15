package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.dto.service.PostOfficeDTO;
import cc.mrbird.febs.rcs.entity.PostOffice;
import cc.mrbird.febs.rcs.mapper.PostOfficeMapper;
import cc.mrbird.febs.rcs.service.IPostOfficeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 合同表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:52
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PostOfficeServiceImpl extends ServiceImpl<PostOfficeMapper, PostOffice> implements IPostOfficeService {

    @Autowired
    PostOfficeMapper postOfficeMapper;

    @Override
    public IPage<PostOffice> findPostOffices(QueryRequest request, PostOffice postOffice) {
        LambdaQueryWrapper<PostOffice> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<PostOffice> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<PostOffice> findPostOffices(PostOffice postOffice) {
	    LambdaQueryWrapper<PostOffice> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPostOffice(PostOffice postOffice) {
        this.save(postOffice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePostOffice(PostOffice postOffice) {
        this.saveOrUpdate(postOffice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePostOffice(PostOffice postOffice) {
        LambdaQueryWrapper<PostOffice> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void savePostOfficeDTO(PostOfficeDTO postOfficeDTO) {
        try {
            PostOffice postOffice = new PostOffice();
            BeanUtils.copyProperties(postOfficeDTO, postOffice);

            postOffice.setCreatedTime(new Date());
            postOffice.setId(postOfficeDTO.getIndex());
            postOffice.setUpdatedTime(new Date());

            this.saveOrUpdate(postOffice);
        } catch (Exception e) {
            throw new RcsApiException(e.getMessage());
        }
    }
}
