package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.service.PostOfficeDTO;
import cc.mrbird.febs.rcs.entity.PostOffice;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 合同表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:52
 */
public interface IPostOfficeService extends IService<PostOffice> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param postOffice postOffice
     * @return IPage<PostOffice>
     */
    IPage<PostOffice> findPostOffices(QueryRequest request, PostOffice postOffice);

    /**
     * 查询（所有）
     *
     * @param postOffice postOffice
     * @return List<PostOffice>
     */
    List<PostOffice> findPostOffices(PostOffice postOffice);

    /**
     * 新增
     *
     * @param postOffice postOffice
     */
    void createPostOffice(PostOffice postOffice);

    /**
     * 修改
     *
     * @param postOffice postOffice
     */
    void updatePostOffice(PostOffice postOffice);

    /**
     * 删除
     *
     * @param postOffice postOffice
     */
    void deletePostOffice(PostOffice postOffice);

    void savePostOfficeDTO(PostOfficeDTO postOfficeDTO);
}
