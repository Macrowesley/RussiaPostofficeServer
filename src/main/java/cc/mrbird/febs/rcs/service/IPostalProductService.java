package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.PostalProduct;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 邮政产品表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:46:18
 */
public interface IPostalProductService extends IService<PostalProduct> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param postalProduct postalProduct
     * @return IPage<PostalProduct>
     */
    IPage<PostalProduct> findPostalProducts(QueryRequest request, PostalProduct postalProduct);

    /**
     * 查询（所有）
     *
     * @param postalProduct postalProduct
     * @return List<PostalProduct>
     */
    List<PostalProduct> findPostalProducts(PostalProduct postalProduct);

    /**
     * 新增
     *
     * @param postalProduct postalProduct
     */
    void createPostalProduct(PostalProduct postalProduct);

    /**
     * 修改
     *
     * @param postalProduct postalProduct
     */
    void updatePostalProduct(PostalProduct postalProduct);

    /**
     * 删除
     *
     * @param postalProduct postalProduct
     */
    void deletePostalProduct(PostalProduct postalProduct);
}
