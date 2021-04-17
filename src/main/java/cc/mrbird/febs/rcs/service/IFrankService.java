package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Frank;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【待定】这个表的父表是哪个？Statistics还是Transaction Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:32
 */
public interface IFrankService extends IService<Frank> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param frank frank
     * @return IPage<Frank>
     */
    IPage<Frank> findFranks(QueryRequest request, Frank frank);

    /**
     * 查询（所有）
     *
     * @param frank frank
     * @return List<Frank>
     */
    List<Frank> findFranks(Frank frank);

    /**
     * 新增
     *
     * @param frank frank
     */
    void createFrank(Frank frank);

    /**
     * 修改
     *
     * @param frank frank
     */
    void updateFrank(Frank frank);

    /**
     * 删除
     *
     * @param frank frank
     */
    void deleteFrank(Frank frank);
}
