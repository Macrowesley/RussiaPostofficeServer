package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.RcsApiErrorEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.service.ContractDTO;
import cc.mrbird.febs.rcs.dto.service.CustomerDTO;
import cc.mrbird.febs.rcs.dto.service.PostOfficeDTO;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.Customer;
import cc.mrbird.febs.rcs.entity.PostOfficeContract;
import cc.mrbird.febs.rcs.mapper.ContractMapper;
import cc.mrbird.febs.rcs.service.*;
import cc.mrbird.febs.rcs.vo.ContractVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    ICustomerService customerService;

    @Autowired
    IPostOfficeService postOfficeService;

    @Autowired
    ContractMapper contractMapper;

    @Autowired
    IPostOfficeContractService postOfficeContractService;

    @Autowired
    IContractAddressService contractAddressService;

    @Override
    public IPage<Contract> findContracts(QueryRequest request, Contract contract) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        if(contract == null){
            contract = new Contract();
        }
        if (StringUtils.isNotBlank(contract.getCode())){
            queryWrapper.eq(Contract::getCode,contract.getCode());
        }

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
    public boolean checkIExist(String code) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Contract::getCode, code);
        return this.baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void saveContractDto(ContractDTO contractDTO) {
        if (checkIExist(contractDTO.getCode())){
            throw new RcsApiException(RcsApiErrorEnum.ContractExist);
        }

        try {
            log.info("接收服务器传递过来的合同数据 开始");

            //保存合同信息
            Contract contract = new Contract();
            BeanUtils.copyProperties(contractDTO, contract);
            contract.setCode(contractDTO.getCode());
            contract.setEnable(contractDTO.isEnable() ? 1 : 0);
            contract.setCurrent(0D);
            contract.setConsolidate(0D);
            contract.setCreatedTime(new Date());
            contract.setUpdatedTime(new Date());
            contract.setModified(DateKit.parseRussiatime(contractDTO.getModified()));
            if (checkIsExist(contractDTO.getCode())) {
                contract.setUpdatedTime(new Date());
            }
            this.saveOrUpdate(contract);

            //保存客户信息
            CustomerDTO customerDTO = contractDTO.getCustomer();
            Customer customer = new Customer();
            customer.setId(customerDTO.getId());
            customer.setContractCode(contract.getCode());
            customer.setInnRu(customerDTO.getInnRu());
            customer.setKppRu(customerDTO.getKppRu());
            customer.setName(customerDTO.getName());
            customer.setLegalAddress(customerDTO.getLegalAddress());
            customer.setOfficeAddress(customerDTO.getOfficeAddress());
            customer.setModified(DateKit.parseRussiatime(customerDTO.getModified()));
            customer.setCreatedTime(new Date());
            customer.setUpdatedTime(new Date());
            customerService.saveOrUpdate(customer);

            //保存合同和邮局关系
            List<PostOfficeContract> postOfficeContractList = new ArrayList<>();
            for (PostOfficeDTO postOfficeDTO : contractDTO.getPostOffices()){
    //            postOfficeService.savePostOfficeDTO(postOfficeDTO);
                PostOfficeContract postOfficeContract = new PostOfficeContract();
                postOfficeContract.setContractCode(contract.getCode());
                postOfficeContract.setPostOfficeId(postOfficeDTO.getIndex());
                postOfficeContractList.add(postOfficeContract);
            }
            postOfficeContractService.saveOrUpdateBatch(postOfficeContractList);
            log.info("接收服务器传递过来的合同数据 结束");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RcsApiException(RcsApiErrorEnum.SaveContractDtoError);
        }
    }

    @Override
    public boolean checkIsExist(String contractCode) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();

        return contractMapper.selectCount(queryWrapper.eq(Contract::getCode, contractCode)) != 0;
    }

    @Override
    public Contract getByConractCode(String contractCode) {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        Contract contract = getOne(queryWrapper.eq(Contract::getCode, contractCode));
        if (contract == null) {
            log.error("Unknown contractCode:" + contractCode);
            throw new FmException(FMResultEnum.ContractNotExist);
        }
        return contract;
    }

    @Override
    public ContractVO getVoByConractCode(String contractCode) {
        Contract contract = getByConractCode(contractCode);
        String addressContent = contractAddressService.selectStrListByConractCode(contractCode);

        ContractVO contractVO = new ContractVO();
        BeanUtils.copyProperties(contract, contractVO);
        contractVO.setAddressContent(addressContent);
        return contractVO;
    }
}
