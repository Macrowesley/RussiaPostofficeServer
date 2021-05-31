package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.service.ContractDTO;
import cc.mrbird.febs.rcs.dto.service.CustomerDTO;
import cc.mrbird.febs.rcs.dto.service.PostOfficeDTO;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.Customer;
import cc.mrbird.febs.rcs.entity.PostOffice;
import cc.mrbird.febs.rcs.entity.PostOfficeContract;
import cc.mrbird.febs.rcs.mapper.ContractMapper;
import cc.mrbird.febs.rcs.service.IContractService;
import cc.mrbird.febs.rcs.service.ICustomerService;
import cc.mrbird.febs.rcs.service.IPostOfficeContractService;
import cc.mrbird.febs.rcs.service.IPostOfficeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {
    private final ICustomerService customerService;
    private final IPostOfficeService postOfficeService;
    private final ContractMapper contractMapper;
    private final IPostOfficeContractService postOfficeContractService;

    @Override
    public IPage<Contract> findContracts(QueryRequest request, Contract contract) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Contract> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Contract> findContracts(Contract contract) {
	    LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createContract(Contract contract) {
        this.save(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContract(Contract contract) {
        this.saveOrUpdate(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContract(Contract contract) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    @Override
    @Transactional(rollbackFor = ApiException.class)
    public void saveContractDto(ContractDTO contractDTO) {
        log.info("接收服务器传递过来的合同数据 开始");

        Contract contract = new Contract();
        BeanUtils.copyProperties(contractDTO,contract);
        contract.setCreatedTime(new Date());
        if (checkIsExist(contractDTO.getId())) {
            contract.setUpdatedTime(new Date());
        }
        this.saveOrUpdate(contract);

        CustomerDTO customerDTO = contractDTO.getCustomer();
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setContractId(contract.getId());
        customer.setInnRu(customerDTO.getInn_ru());
        customer.setKppRu(customerDTO.getKpp_ru());
        customer.setName(customerDTO.getName());
        customer.setCreatedTime(new Date());
        customer.setUpdatedTime(new Date());
        customerService.saveOrUpdate(customer);

        List<PostOfficeContract> postOfficeContractList = new ArrayList<>();
        for (PostOfficeDTO postOfficeDTO : contractDTO.getPostOffice()){
//            postOfficeService.savePostOfficeDTO(postOfficeDTO);
            PostOfficeContract postOfficeContract = new PostOfficeContract();
            postOfficeContract.setContractId(contract.getId());
            postOfficeContract.setPostOfficeId(postOfficeDTO.getIndex());
            postOfficeContractList.add(postOfficeContract);
        }
        postOfficeContractService.saveBatch(postOfficeContractList);
        log.info("接收服务器传递过来的合同数据 结束");
    }

    @Override
    public boolean checkIsExist(String contractId) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();

        return contractMapper.selectCount(queryWrapper.eq(Contract::getId, contractId)) != 0;
    }

    @Override
    public Contract getByConractId(String contractId) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        return getOne(queryWrapper.eq(Contract::getId, contractId));
    }
}
