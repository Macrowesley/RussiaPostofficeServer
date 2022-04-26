package cc.mrbird.febs.rcs.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
//@Excel("printJob Excel")
public class PrintJobExcelVO2 {

/*    *//**
     * 打印开始前，合同剩余的钱
     * excel倒数第二行，汇总金额
     *//*
    @ExcelProperty(value = "Total amount of the of money in the conract (rest of money at the start of period), rubbles")
    Double aPrintBeginContractMoney;

    *//**
     * 客户名
     * 倒数第二行：Total under the contract
     * 倒数第一行：Total amount of money for all contracts of the organiztions for the reporting period
     *//*
    @ExcelProperty(value = "Name of organization")
    String bCustomerName;

    @ExcelProperty(value = "Taxpayer Identification Number of oragnization")
    String cCustomerinnRu;

    @ExcelProperty(value = "Industrial Enterprises Classifier of organization")
    String dCustomerKppRu;

    @ExcelProperty(value = "Number of contract")
    String eContractName;

    @ExcelProperty(value = "№ Electrical metet (counter)")
    String fFrankMachineId;

    @ExcelProperty(value = "List number")
    String gListNumber;

    @ExcelProperty(value = "Start date")
    String hStartDate;

    @ExcelProperty(value = "Start time")
    String iStartTime;

    @ExcelProperty(value = "End date")
    String jEndDate;

    @ExcelProperty(value = "End time")
    String kEndTime;

    @ExcelProperty(value = "Postage type")
    String lTaxRegionType;

    @ExcelProperty(value = "Postage category")
    String mTaxLabelRu;

    *//**
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     *//*
    @ExcelProperty(value = "Quantity in order")
    Integer nForeseenOneBatchCount;

    *//**
     * 倒数第二行： = 倒数第二行之前的所有数据之和
     * 倒数第一行： = 倒数第二行数据
     *//*
    @ExcelProperty(value = "Wight, gram")
    Integer oForeseenOneBatchWeight;

    @ExcelProperty(value = "Sale rate by order (tariff), rubbles")
    String pTaxFixedValue;


    *//**
     * foreseenOneBatchCount * taxFixedValue
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     *//*
    @ExcelProperty(value = "Amount sum of money, rubbles")
    Double qCallculationAmount;

    @ExcelProperty(value = "Quantity in order (real quantity - fact)")
    String rTransactionOneBatchCount;

    *//**
     * foreseenOneBatchCount * taxFixedValue
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     *//*
    @ExcelProperty(value = "Wight (real wight - fact), gram")
    Integer sTransactionOneBatchWeight;

    *//**
     *  = taxFixedValue
     *//*
    @ExcelProperty(value = "Sale rate by order (real sale rate - fact), rubbles")
    String tTaxRealSaleRate;


    *//**
     * TransactionOneBatchCount * taxRealSaleRate
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     *//*
    @ExcelProperty(value = "Amount sum of money (real sum of money - fact), rubbles")
    Double uCallculationRealSumMoney;

    *//**
     * callculationAmount - callculationRealSumMoney
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     *//*
    @ExcelProperty(value = "The amount of the of money in the conract (rest of money), rubbles ")
    Double vCallculationRealRestMoney;

    *//**
     * printBeginContractMoney - callculationRealSumMoney
     * 倒数第二行： = 第一行数据
     * 倒数第一行： = 倒数第二行之前的所有数据之和
     *//*
    @ExcelProperty(value = "Total amount of the of money in the conract (rest of money at end of period), rubbles ")
    Double wPrintEndContractMoney;

    *//**
     * 额外信息，用于辅助，不用于excel
     *//*
    String code;
    Long startMsgId;*/
}
