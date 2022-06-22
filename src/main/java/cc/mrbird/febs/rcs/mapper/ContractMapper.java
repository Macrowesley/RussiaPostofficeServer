package cc.mrbird.febs.rcs.mapper;

import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.ContractCustomerInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * 合同表 Mapper
 *
 * @author mrbird
 * @date 2021-04-17 14:45:48
 */
public interface ContractMapper extends BaseMapper<Contract> {

    /**
     * 查询合同，客户信息
     * @param code
     * @return
     */
    @Select("select t1.code as code, t1.name as name, t2.name as customer_name, t2.inn_ru, t2.kpp_ru  from rcs_contract t1 left join rcs_customer t2 on t1.code = t2.contract_code where t1.code=#{code}")
    @Results({
            @Result(column = "code", property = "contractCode"),
            @Result(column = "name", property = "contractName"),
            @Result(column = "customer_name", property = "customerName"),
            @Result(column = "inn_ru", property = "customerinnRu"),
            @Result(column = "kpp_ru", property = "customerKppRu")
    })
    ContractCustomerInfo findContractAndCustomer(@Param("code") String code);
}
