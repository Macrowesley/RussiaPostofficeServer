package cc.mrbird.febs.notice.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.notice.entity.Notice;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 消息提示表 Service接口
 *
 * @author mrbird
 * @date 2020-06-11 15:30:26
 */
public interface INoticeService extends IService<Notice> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param notice notice
     * @return IPage<Notice>
     */
    IPage<Notice> findNotices(QueryRequest request, Notice notice);

    /**
     * 查询（所有）
     *
     * @param notice notice
     * @return List<Notice>
     */
    List<Notice> findNotices(Notice notice);

    /**
     * 查询是否有新的提醒
     * @return
     */
    boolean findHasNewNotices();

    /**
     * 新增
     *
     * @param notice notice
     */
    void createNotice(Notice notice);

    /**
     * 修改
     *
     * @param notice notice
     */
    void updateNotice(Notice notice);

    /**
     * 删除
     *
     * @param notice notice
     */
    void deleteNotice(Notice notice);

    /**
     * 用户下所有的消息标位已读
     */
    void readAllNotice();
}
