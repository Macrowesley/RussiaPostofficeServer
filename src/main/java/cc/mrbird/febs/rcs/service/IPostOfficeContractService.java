package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.PostOfficeContract;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 邮局-合同关系表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:46:07
 */
public interface IPostOfficeContractService extends IService<PostOfficeContract> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param postOfficeContract postOfficeContract
     * @return IPage<PostOfficeContract>
     */
    IPage<PostOfficeContract> findPostOfficeContracts(QueryRequest request, PostOfficeContract postOfficeContract);

    /**
     * 查询（所有）
     *
     * @param postOfficeContract postOfficeContract
     * @return List<PostOfficeContract>
     */
    List<PostOfficeContract> findPostOfficeContracts(PostOfficeContract postOfficeContract);

    /**
     * 新增
     *
     * @param postOfficeContract postOfficeContract
     */
    void createPostOfficeContract(PostOfficeContract postOfficeContract);

    /**
     * 修改
     *
     * @param postOfficeContract postOfficeContract
     */
    void updatePostOfficeContract(PostOfficeContract postOfficeContract);

    /**
     * 删除
     *
     * @param postOfficeContract postOfficeContract
     */
    void deletePostOfficeContract(PostOfficeContract postOfficeContract);
}
