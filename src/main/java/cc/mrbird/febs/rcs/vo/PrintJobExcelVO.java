package cc.mrbird.febs.rcs.vo;

import cc.mrbird.febs.rcs.entity.Customer;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Excel("printJob Excel")
public class PrintJobExcelVO {

    /**
     * 打印开始前，合同剩余的钱
     * excel倒数第二行，汇总金额
     */
    @ExcelField(value = "Total amount of the of money in the conract (rest of money at the start of period), rubbles")
    Double aPrintBeginContractMoney;

    /**
     * 客户名
     * 倒数第二行：Total under the contract
     * 倒数第一行：Total amount of money for all contracts of the organiztions for the reporting period
     */
    @ExcelField(value = "Name of organization")
    String bCustomerName;

    @ExcelField(value = "Taxpayer Identification Number of oragnization")
    String cCustomerinnRu;

    @ExcelField(value = "Industrial Enterprises Classifier of organization")
    String dCustomerKppRu;

    @ExcelField(value = "Number of contract")
    String eContractName;

    @ExcelField(value = "№ Electrical metet (counter)")
    String fFrankMachineId;

    @ExcelField(value = "List number")
    String gListNumber;

    @ExcelField(value = "Start date")
    String hStartDate;

    @ExcelField(value = "Start time")
    String iStartTime;

    @ExcelField(value = "End date")
    String jEndDate;

    @ExcelField(value = "End time")
    String kEndTime;

    @ExcelField(value = "Postage type")
    String lTaxRegionType;

    @ExcelField(value = "Postage category")
    String mTaxLabelRu;

    /**
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     */
    @ExcelField(value = "Quantity in order")
    Integer nForeseenOneBatchCount;

    /**
     * 倒数第二行： = 倒数第二行之前的所有数据之和
     * 倒数第一行： = 倒数第二行数据
     */
    @ExcelField(value = "Wight, gram")
    Integer oForeseenOneBatchWeight;

    @ExcelField(value = "Sale rate by order (tariff), rubbles")
    String pTaxFixedValue;


    /**
     * foreseenOneBatchCount * taxFixedValue
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     */
    @ExcelField(value = "Amount sum of money, rubbles")
    Double qCallculationAmount;

    @ExcelField(value = "Quantity in order (real quantity - fact)")
    String rTransactionOneBatchCount;

    /**
     * foreseenOneBatchCount * taxFixedValue
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     */
    @ExcelField(value = "Wight (real wight - fact), gram")
    Integer sTransactionOneBatchWeight;

    /**
     *  = taxFixedValue
     */
    @ExcelField(value = "Sale rate by order (real sale rate - fact), rubbles")
    String tTaxRealSaleRate;


    /**
     * TransactionOneBatchCount * taxRealSaleRate
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     */
    @ExcelField(value = "Amount sum of money (real sum of money - fact), rubbles")
    Double uCallculationRealSumMoney;

    /**
     * callculationAmount - callculationRealSumMoney
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     */
    @ExcelField(value = "The amount of the of money in the conract (rest of money), rubbles ")
    Double vCallculationRealRestMoney;

    /**
     * printBeginContractMoney - callculationRealSumMoney
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     */
    @ExcelField(value = "Total amount of the of money in the conract (rest of money at end of period), rubbles ")
    Double wPrintEndContractMoney;

    /**
     * 额外信息，用于辅助，不用于excel
     */
    String code;
    Long startMsgId;
}
