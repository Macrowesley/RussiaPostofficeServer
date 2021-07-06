package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.dto.service.ContractDTO;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.vo.ContractVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 合同表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:48
 */
public interface IContractService extends IService<Contract> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param contract contract
     * @return IPage<Contract>
     */
    IPage<Contract> findContracts(QueryRequest request, Contract contract);

    /**
     * 查询（所有）
     *
     * @param contract contract
     * @return List<Contract>
     */
    List<Contract> findContracts(Contract contract);

    /**
     * 新增
     *
     * @param contract contract
     */
    void createContract(Contract contract);

    /**
     * 修改
     *
     * @param contract contract
     */
    void updateContract(Contract contract);

    /**
     * 删除
     *
     * @param contract contract
     */
    void deleteContract(Contract contract);

    boolean checkIExist(String code);

    void saveContractDto(ContractDTO contractDTO);

    boolean checkIsExist(String contractCode);

    Contract getByConractCode(String contractCode);

    /**
     * 获取详情界面显示的结构体
     * @param contractCode
     * @return
     */
    ContractVO getVoByConractCode(String contractCode);
}
