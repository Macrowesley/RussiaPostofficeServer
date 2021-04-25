package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.manager.PublicKeyDTO;
import cc.mrbird.febs.rcs.entity.PublicKey;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 公钥表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:23
 */
public interface IPublicKeyService extends IService<PublicKey> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param publicKey publicKey
     * @return IPage<PublicKey>
     */
    IPage<PublicKey> findPublicKeys(QueryRequest request, PublicKey publicKey);

    /**
     * 查询（所有）
     *
     * @param publicKey publicKey
     * @return List<PublicKey>
     */
    List<PublicKey> findPublicKeys(PublicKey publicKey);

    /**
     * 新增
     *
     * @param publicKey publicKey
     */
    void createPublicKey(PublicKey publicKey);

    /**
     * 修改
     *
     * @param publicKey publicKey
     */
    void updatePublicKey(PublicKey publicKey);

    /**
     * 删除
     *
     * @param publicKey publicKey
     */
    void deletePublicKey(PublicKey publicKey);

    void saveOrUpdate(String frankMachineId, PublicKeyDTO publicKeyDTO);
}
