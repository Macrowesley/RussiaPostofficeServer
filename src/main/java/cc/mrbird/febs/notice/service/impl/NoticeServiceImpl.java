package cc.mrbird.febs.notice.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.notice.entity.Notice;
import cc.mrbird.febs.notice.mapper.NoticeMapper;
import cc.mrbird.febs.notice.service.INoticeService;
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
 * 消息提示表 Service实现
 *
 * @author mrbird
 * @date 2020-06-11 15:30:26
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public IPage<Notice> findNotices(QueryRequest request, Notice notice) {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getUserId, FebsUtil.getCurrentUser().getUserId());
        queryWrapper.eq(Notice::getIsRead, "0");
        Page<Notice> page = new Page<>();
        SortUtil.handlePageSort(request, page, "notice_id" , FebsConstant.ORDER_DESC, false);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Notice> findNotices(Notice notice) {
	    LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
		return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询是否有新的提醒
     *
     * @return
     */
    @Override
    public boolean findHasNewNotices() {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getUserId, FebsUtil.getCurrentUser().getUserId());
        queryWrapper.eq(Notice::getIsRead, "0");
        int count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotice(Notice notice) {
        this.save(notice);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotice(Notice notice) {
        this.saveOrUpdate(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotice(Notice notice) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
	    // 设置删除条件
	    this.remove(wrapper);
	}

    /**
     * 用户下所有的消息标位已读
     */
    @Override
    public void readAllNotice() {

        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getUserId, FebsUtil.getCurrentUser().getUserId());
        queryWrapper.eq(Notice::getIsRead, "0");
        Notice notice = new Notice();
        notice.setIsRead("1");
        baseMapper.update(notice, queryWrapper);

    }
}
