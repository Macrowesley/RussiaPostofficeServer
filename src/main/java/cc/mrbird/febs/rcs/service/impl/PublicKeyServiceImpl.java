package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.common.kit.PublicKeyGenerate;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.mapper.PublicKeyMapper;
import cc.mrbird.febs.rcs.service.IPublicKeyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    PublicKeyMapper publicKeyMapper;

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
    public PublicKey saveOrUpdatePublicKey(String frankMachineId) {
        PublicKey dbPublicKey = findByFrankMachineId(frankMachineId);
        //如果数据库没有这个机器的publickey，那就更新
        //如果数据库有，但是这个publickey是也已经闭环了，也更新
        //否则，返回正在处理的秘钥
        if (dbPublicKey != null && dbPublicKey.getFlow() == FlowEnum.FlowIng.getCode()){
            return dbPublicKey;
        }

        try {
            log.info("更新服务器public 开始");
            int revision = dbPublicKey == null ? 1 : dbPublicKey.getRevision() + 1;

            PublicKeyGenerate publicKeyGenerate = new PublicKeyGenerate();
            //更新publickey
            int expire = 365*3;
            PublicKey publicKey = new PublicKey();
            publicKey.setFrankMachineId(frankMachineId);
            publicKey.setPublicKey(publicKeyGenerate.getPublicKey());
            publicKey.setPrivateKey(publicKeyGenerate.getPrivateKey());
            publicKey.setRevision(revision);
            publicKey.setAlg("");
            publicKey.setExpireTime(DateKit.offsetDayDate(expire));
            publicKey.setFlow(FlowEnum.FlowIng.getCode());
            publicKey.setFlowDetail(FlowDetailEnum.PublicKeyBegin.getCode());
            publicKey.setCreatedTime(new Date());
            this.saveOrUpdate(publicKey);

            return publicKey;
        } catch (Exception e) {
            throw new RcsApiException(e.getMessage());
        }
    }

    @Override
    public PublicKey findByFrankMachineId(String frankMachineId) {
        LambdaQueryWrapper<PublicKey> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PublicKey::getFrankMachineId, frankMachineId);
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public boolean checkFmIsUpdate(String frankMachineId) {
        LambdaQueryWrapper<PublicKey> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PublicKey::getFrankMachineId, frankMachineId);
        wrapper.select(PublicKey::getFlow);
        return this.baseMapper.selectOne(wrapper).getFlow() == FlowEnum.FlowEnd.getCode();
    }

    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeFlowInfo(PublicKey dbPubliceKey, FlowDetailEnum curFlowDetail) {
        switch (curFlowDetail){
            case PublicKeyEndSuccess:
                dbPubliceKey.setFlow(FlowEnum.FlowEnd.getCode());
                break;
            case PublicKeyErrorFail4xxError:
                break;
            case PublicKeyErrorFailUnKnow:
                break;
        }
        dbPubliceKey.setFlowDetail(curFlowDetail.getCode());
        this.saveOrUpdate(dbPubliceKey);
    }
}
