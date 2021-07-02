package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.netty.protocol.dto.AddressDTO;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.dto.service.ContractAddressDTO;
import cc.mrbird.febs.rcs.entity.ContractAddress;
import cc.mrbird.febs.rcs.mapper.ContractAddressMapper;
import cc.mrbird.febs.rcs.service.IContractAddressService;
import cc.mrbird.febs.rcs.service.IContractService;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
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
@Validated
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ContractAddressServiceImpl extends ServiceImpl<ContractAddressMapper, ContractAddress> implements IContractAddressService {

    @Autowired
    IDeviceService deviceService;

    @Autowired
    IContractService contractService;


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
    @Transactional(rollbackFor = Exception.class)
    public void deleteByContractCode(String contractCode) {
        LambdaQueryWrapper<ContractAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractAddress::getContractCode, contractCode);
        this.remove(wrapper);
    }


    @Override
    public List<ContractAddress> selectListByConractCode(String contractCode) {
        LambdaQueryWrapper<ContractAddress> queryWrapper = new LambdaQueryWrapper<>();
        return this.baseMapper.selectList(queryWrapper.eq(ContractAddress::getContractCode, contractCode));
    }

    @Override
    public boolean checkIsExist(String contractCode) {
        LambdaQueryWrapper<ContractAddress> queryWrapper = new LambdaQueryWrapper<>();
        return this.baseMapper.selectCount(queryWrapper.eq(ContractAddress::getContractCode, contractCode)) > 0;
    }

    @Override
    public AddressDTO[] selectArrayByConractCode(String contractCode) {
        return selectListByConractCode(contractCode).stream().map(contractAddress -> {
            return new AddressDTO(contractAddress.getAddress());
        }).collect(Collectors.toList()).stream().toArray(AddressDTO[]::new);
    }

    @Override
    public String selectStrListByConractCode(String contractCode) {
        StringBuilder stringBuilder = new StringBuilder();
        selectListByConractCode(contractCode).stream().forEach(contractAddress -> {
            stringBuilder.append(contractAddress.getAddress() + ", ");
        });
        return stringBuilder.subSequence(0,stringBuilder.length()-2).toString();
    }

    /**
     * 批量添加地址
     * @param addressList
     * @param contractCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAddressList(List<String> addressList, String contractCode) {
        List<ContractAddress> list = new ArrayList<>();
        addressList.forEach(item ->{
            if (item.length() > 10) {
                ContractAddress bean = new ContractAddress();
                bean.setContractCode(contractCode);
                bean.setAddress(item);
                bean.setCreatedTime(new Date());
                list.add(bean);
            }
        });
        this.saveBatch(list);
    }

    /**
     * 保存页面添加的地址列表
     *
     * @param contractAddressDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = FebsException.class)
    public FebsResponse addAddressList(ContractAddressDTO contractAddressDTO) {
        try {
            String frankMachineId = contractAddressDTO.getFMid();
            String contractCode = contractAddressDTO.getContractCode();
            String addressListStr = contractAddressDTO.getAddressList().trim();
            String split = ";";

            if (StringUtils.isEmpty(frankMachineId) || StringUtils.isEmpty(contractCode) ||StringUtils.isEmpty(addressListStr) ){
                throw new Exception("不能为空");
            }

            //验证数据是否正常
            if (addressListStr.length() > 200 && !addressListStr.contains(split)){
                throw new Exception("多个地址必须通过分隔符"+split+"分隔开");
            }

            if (!contractService.checkIsExist(contractCode)){
                throw new Exception("ContractCode: "+contractCode+" 不存在");
            }

            if (!deviceService.checkByFmId(frankMachineId)){
                throw new Exception("frankMachineId: "+frankMachineId+" 不存在");
            }

            //删除旧数据
            if (checkIsExist(contractCode)){
                deleteByContractCode(contractCode);
            }
            //分隔数据
            List<String> addressList = Arrays.asList(addressListStr.split(split));

            //保存数据
            saveAddressList(addressList,contractCode);

            return new FebsResponse().success();
        }catch (Exception e){
            return new FebsResponse().fail().message(e.getMessage());
        }
    }
}
