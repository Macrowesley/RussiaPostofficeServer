package cc.mrbird.febs.rcs.entity;


import lombok.Data;
import lombok.ToString;

/**
 * 合同号，客户信息汇总
 */
@Data
@ToString
public class ContractCustomerInfo {
    String customerName;

    String customerinnRu;

    String customerKppRu;

    String contractName;

    String contractCode;;
}
