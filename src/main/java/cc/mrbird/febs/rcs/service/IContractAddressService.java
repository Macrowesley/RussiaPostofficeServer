package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.netty.protocol.dto.AddressDTO;
import cc.mrbird.febs.rcs.dto.service.ContractAddressDTO;
import cc.mrbird.febs.rcs.entity.ContractAddress;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 合同表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:48
 */
public interface IContractAddressService extends IService<ContractAddress> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param contractAddress contractAddress
     * @return IPage<ContractAddress>
     */
    IPage<ContractAddress> findContractAddressPage(QueryRequest request, ContractAddress contractAddress);

    /**
     * 查询（所有）
     *
     * @param contractAddress contractAddress
     * @return List<ContractAddress>
     */
    List<ContractAddress> findContractAddressPage(ContractAddress contractAddress);

    /**
     * 新增
     *
     * @param contractAddress contractAddress
     */
    void createContractAddress(ContractAddress contractAddress);

    /**
     * 修改
     *
     * @param contractAddress contractAddress
     */
    void updateContractAddress(ContractAddress contractAddress);

    /**
     * 删除
     *
     * @param contractAddress contractAddress
     */
    void deleteContractAddress(ContractAddress contractAddress);

    void deleteByContractId(String contractId);

    List<ContractAddress> selectListByConractId(String contractId);

    boolean checkIsExist(String contractId);

    AddressDTO[] selectArrayByConractId(String contractId);

    String selectStrListByConractId(String contractId);

    void saveAddressList(List<String> addressList, String contractId);

    /**
     * 保存页面添加的地址列表
     * @param contractAddressDTO
     * @return
     */
    FebsResponse addAddressList(ContractAddressDTO contractAddressDTO);
}
