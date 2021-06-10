package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.netty.protocol.dto.AddressDTO;
import cc.mrbird.febs.rcs.entity.ContractAddress;
import cc.mrbird.febs.rcs.mapper.ContractAddressMapper;
import cc.mrbird.febs.rcs.service.IContractAddressService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 合同表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:48
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ContractAddressServiceImpl extends ServiceImpl<ContractAddressMapper, ContractAddress> implements IContractAddressService {

    @Override
    public IPage<ContractAddress> findContractAddressPage(QueryRequest request, ContractAddress contract) {
        LambdaQueryWrapper<ContractAddress> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<ContractAddress> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<ContractAddress> findContractAddressPage(ContractAddress contract) {
	    LambdaQueryWrapper<ContractAddress> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createContractAddress(ContractAddress contract) {
        this.save(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContractAddress(ContractAddress contract) {
        this.saveOrUpdate(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContractAddress(ContractAddress contract) {
        LambdaQueryWrapper<ContractAddress> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}


    @Override
    public List<ContractAddress> selectListByConractId(String contractId) {
        LambdaQueryWrapper<ContractAddress> queryWrapper = new LambdaQueryWrapper<>();
        return this.baseMapper.selectList(queryWrapper.eq(ContractAddress::getContractId, contractId));
    }

    @Override
    public AddressDTO[] selectArrayByConractId(String contractId) {
        return selectListByConractId(contractId).stream().map(contractAddress -> {
            return new AddressDTO(contractAddress.getAddress());
        }).collect(Collectors.toList()).stream().toArray(AddressDTO[]::new);
    }

    @Override
    public String selectStrListByConractId(String contractId) {
        StringBuilder stringBuilder = new StringBuilder();
        selectListByConractId(contractId).stream().forEach(contractAddress -> {
            stringBuilder.append(contractAddress.getAddress() + ", ");
        });
        return stringBuilder.subSequence(0,stringBuilder.length()-2).toString();
    }

    /**
     * 批量添加地址
     * @param addressList
     * @param contractId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAddressList(List<String> addressList, String contractId) {
        List<ContractAddress> list = new ArrayList<>();
        addressList.forEach(item ->{
            ContractAddress bean = new ContractAddress();
            bean.setContractId(contractId);
            bean.setAddress(item);
            bean.setCreatedTime(new Date());
            list.add(bean);
        });
        this.saveBatch(list);
    }
}
