package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.common.kit.PublicKeyGenerate;
import cc.mrbird.febs.rcs.dto.manager.PublicKeyDTO;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.mapper.PublicKeyMapper;
import cc.mrbird.febs.rcs.service.IPublicKeyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 公钥表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:23
 */
@Slf4j
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



    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public PublicKeyDTO saveOrUpdatePublicKey(String frankMachineId) {
        try {
            log.info("更新服务器public 开始");
            PublicKey dbPublicKey = this.getById(frankMachineId);
            int revision = dbPublicKey == null ? 1 : dbPublicKey.getRevision() + 1;

            PublicKeyGenerate publicKeyGenerate = new PublicKeyGenerate();
            //更新publickey
            int expire = 365;
            PublicKey publicKey = new PublicKey();
            publicKey.setFrankMachineId(frankMachineId);
            publicKey.setPublicKey(publicKeyGenerate.getPublicKey());
            publicKey.setPrivateKey(publicKeyGenerate.getPrivateKey());
            publicKey.setRevision(revision);
            publicKey.setAlg("");
            publicKey.setExpireTime(DateKit.offsetDayDate(expire));
            publicKey.setCreatedTime(new Date());
            this.saveOrUpdate(publicKey);

            //返回给俄罗斯
            PublicKeyDTO publicKeyDTO = new PublicKeyDTO();
            publicKeyDTO.setKey("-----BEGIN PUBLIC KEY----- " + publicKey.getPublicKey() + " -----END PUBLIC KEY-----");
            publicKeyDTO.setExpireDate(DateKit.offsetDayDateStr(expire));
            publicKeyDTO.setRevision(revision);
            publicKeyDTO.setAlg(publicKey.getAlg());
            log.info("更新服务器public 数据库 结束");
            return publicKeyDTO;
        } catch (Exception e) {
            throw new RcsApiException(e.getMessage());
        }
    }
}
