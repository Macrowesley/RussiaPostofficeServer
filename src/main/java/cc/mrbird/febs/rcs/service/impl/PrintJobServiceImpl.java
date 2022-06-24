package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFMDTO;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.enums.PrintJobTypeEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.dto.machine.PrintProgressInfo;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmDto;
import cc.mrbird.febs.rcs.dto.manager.ManagerBalanceDTO;
import cc.mrbird.febs.rcs.dto.manager.TransactionDTO;
import cc.mrbird.febs.rcs.dto.service.PrintJobDTO;
import cc.mrbird.febs.rcs.dto.ui.PrintJobReq;
import cc.mrbird.febs.rcs.entity.*;
import cc.mrbird.febs.rcs.mapper.PrintJobMapper;
import cc.mrbird.febs.rcs.service.*;
import cc.mrbird.febs.rcs.vo.PrintJobExcelVO;
import cc.mrbird.febs.system.entity.User;
import cn.hutool.core.date.DateUtil;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * 打印任务表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:44:46
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PrintJobServiceImpl extends ServiceImpl<PrintJobMapper, PrintJob> implements IPrintJobService {
    @Autowired
    MessageUtils messageUtils;

    @Autowired
    PrintJobMapper printJobMapper;

    @Autowired
    ITransactionMsgService transactionMsgService;

    @Autowired
    ServiceToMachineProtocol serviceToMachineProtocol;

    @Autowired
    IForeseenService foreseenService;
    @Autowired
    IForeseenProductService foreseenProductService;
    @Autowired
    ITransactionService transactionService;
    @Autowired
    IDeviceService deviceService;
    @Autowired
    IContractService contractService;
    @Autowired
    IBalanceService balanceService;

    @Autowired
    INoticeFrontService noticeFrontService;

    @Override
    public IPage<PrintJob> findPrintJobs(QueryRequest request, PrintJobDTO printJobDto) {
        LambdaQueryWrapper<PrintJob> queryWrapper = new LambdaQueryWrapper<>();
        buildCondition(printJobDto, queryWrapper);
        queryWrapper.orderByDesc(PrintJob::getId);

        Page<PrintJob> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    private void buildCondition(PrintJobDTO printJobDto, LambdaQueryWrapper<PrintJob> queryWrapper) {
        if (StringUtils.isNotBlank(printJobDto.getContractCode())) {
            queryWrapper.eq(PrintJob::getContractCode, printJobDto.getContractCode());
        }

        //printJobDto.getFlow()可能为0、1、2、3
        //0为notBegin,1为flowing,2为successFlowed,3为failFlowed
        if (null != printJobDto.getFlow()) {
            if (0 == printJobDto.getFlow()) {
                queryWrapper.eq(PrintJob::getFlowDetail, 70);
                queryWrapper.eq(PrintJob::getFlow, 0);
            } else if (2 == printJobDto.getFlow()) {
                queryWrapper.eq(PrintJob::getFlowDetail, 61);
                queryWrapper.eq(PrintJob::getFlow, 1);
            } else if (3 == printJobDto.getFlow()) {
                queryWrapper.eq(PrintJob::getFlowDetail, 62);
                queryWrapper.eq(PrintJob::getFlow, 1);
            } else if (4 == printJobDto.getFlow()) {
                queryWrapper.eq(PrintJob::getFlowDetail, 63);
                queryWrapper.eq(PrintJob::getFlow, 1);
            } else if (1 == printJobDto.getFlow()) {
                queryWrapper.notIn(PrintJob::getFlowDetail, 70, 61, 62, 63);
                queryWrapper.eq(PrintJob::getFlow, 0);
            }
        }
        if (StringUtils.isNotBlank(printJobDto.getForeseenId())) {
            queryWrapper.eq(PrintJob::getForeseenId, printJobDto.getForeseenId());
        }
        if (StringUtils.isNotBlank(printJobDto.getFrankMachineId())) {
            queryWrapper.eq(PrintJob::getFrankMachineId, printJobDto.getFrankMachineId());
        }

        //如果时间范围不为空，查询闭环的订单
        String startDate = printJobDto.getStartDate();
        String endData = printJobDto.getEndData();
        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endData)){
            queryWrapper.eq(PrintJob::getFlow, 1);
            queryWrapper.gt(PrintJob::getCreatedTime, startDate);
            queryWrapper.le(PrintJob::getCreatedTime, endData);
        }


        try {
            User currentUser = FebsUtil.getCurrentUser();
            if (currentUser != null && !currentUser.getRoleId().equals(RoleType.systemManager)) {
                queryWrapper.eq(PrintJob::getPcUserId, currentUser.getUserId());
                //普通用户只能查询网络订单，超级管理员所有订单都能查询
                queryWrapper.eq(PrintJob::getType, PrintJobTypeEnum.Web.getCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<PrintJob> findPrintJobs(PrintJobDTO printJobDto) {
        LambdaQueryWrapper<PrintJob> queryWrapper = new LambdaQueryWrapper<>();
        buildCondition(printJobDto, queryWrapper);
        queryWrapper.orderByAsc(PrintJob::getId);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 返回excel需要的数据
     *
     * @param queryRequest
     * @param printJob
     * @return
     */
    @Override
    public List<PrintJobExcelVO> selectExcelData(PrintJobDTO printJobDto) {
        log.info("excel搜索条件：printJobDto = " + printJobDto.toString());
        List<PrintJob> printJobList = findPrintJobs(printJobDto);
        int printJobSize = printJobList.size();
        log.info("printJobList.size = " + printJobSize);

        ArrayList<PrintJobExcelVO> tempList = new ArrayList<>();
        ArrayList<PrintJobExcelVO> printJobExcelVOList = new ArrayList<>();


        //倒数第二行数据
        double a2 = 0,q2 = 0,u2 = 0,v2 = 0,w2 = 0;
        int n2 = 0,o2 = 0,s2 = 0;
        //倒数第1行数据
        double a1 = 0,q1 = 0,u1 = 0,v1 = 0,w1 = 0;
        int n1 = 0,o1 = 0,s1 = 0;


        for (int m = 0; m < printJobSize; m++) {
            PrintJob  bean = printJobList.get(m);
//            log.info("/n ============");
//            log.info("每个订单的信息" + bean.toString());

            //一次打印任务的批次信息
            long t1 = System.currentTimeMillis();
            List<TransactionMsgBatchInfo> onePrintJobTransactionProuductList = transactionMsgService.mergeMsgList(bean.getTransactionId());
//            log.info("合并一次打印任务批次信息耗时{}",(System.currentTimeMillis() -t1));

            //获取合同和客户信息
            ContractCustomerInfo customerInfo = contractService.findContractAndCustomer(bean.getContractCode());
//            log.info(customerInfo.toString());

            int size = onePrintJobTransactionProuductList.size();
            for (int i = 0; i < size; i++) {
                //一个批次的信息
                TransactionMsgBatchInfo batchBean = onePrintJobTransactionProuductList.get(i);
                log.info("每个订单的批次信息：batchBean = " + batchBean.toString());
                PrintJobExcelVO excelVO = new PrintJobExcelVO();
                //printjob添加一个字段，初期金额
                excelVO.setAPrintBeginContractMoney(bean.getContractCurrent());
                excelVO.setBCustomerName(customerInfo.getCustomerName());
                excelVO.setCCustomerinnRu(customerInfo.getCustomerinnRu());
                excelVO.setDCustomerKppRu(customerInfo.getCustomerKppRu());
                excelVO.setEContractName(customerInfo.getContractName());
                excelVO.setFFrankMachineId(bean.getFrankMachineId());
                excelVO.setGListNumber(String.valueOf(i+1));
                excelVO.setHStartDate(DateUtil.format(batchBean.getStartDate(),"yyyy/MM/dd"));
                excelVO.setIStartTime(DateUtil.format(batchBean.getStartDate(),"HH:mm:ss"));
                excelVO.setJEndDate(DateUtil.format(batchBean.getEndDate(),"yyyy/MM/dd"));
                excelVO.setKEndTime(DateUtil.format(batchBean.getEndDate(),"HH:mm:ss"));
                excelVO.setLTaxRegionType(batchBean.getTaxRegionType());
                excelVO.setMTaxLabelRu(batchBean.getTaxLabelRu());
                excelVO.setNForeseenOneBatchCount(batchBean.getForeseenOneBatchCount());
                excelVO.setOForeseenOneBatchWeight(batchBean.getForeseenOneBatchWeight());
                excelVO.setPTaxFixedValue(String.valueOf(batchBean.getTaxFixedValue()));
                excelVO.setQCallculationAmount(batchBean.getCallculationAmount());
                excelVO.setRTransactionOneBatchCount(String.valueOf(batchBean.getTransactionOneBatchCount()));
                excelVO.setSTransactionOneBatchWeight(batchBean.getTransactionOneBatchWeight());
                excelVO.setTTaxRealSaleRate(String.valueOf(batchBean.getTaxRealSaleRate()));
                //TransactionOneBatchCount * taxRealSaleRate
                excelVO.setUCallculationRealSumMoney((double) (batchBean.getTransactionOneBatchCount() * batchBean.getTaxRealSaleRate()));
                //callculationAmount - callculationRealSumMoney
                excelVO.setVCallculationRealRestMoney(excelVO.getQCallculationAmount() - excelVO.getUCallculationRealSumMoney());
                //printBeginContractMoney - callculationRealSumMoney
                excelVO.setWPrintEndContractMoney(excelVO.getAPrintBeginContractMoney() - excelVO.getUCallculationRealSumMoney());

                //添加额外信息
                excelVO.setCode(batchBean.getCode());
                excelVO.setStartMsgId(batchBean.getStartMsgId());
                tempList.add(excelVO);
                log.info("Excel每行信息 excelVO = " + excelVO.toString());

                //累加倒数第一行的结果
                a1 = a1 + excelVO.getAPrintBeginContractMoney();
                n1 = n1 + excelVO.getNForeseenOneBatchCount();
                q1 = q1 + excelVO.getQCallculationAmount();
                s1 = s1 + excelVO.getSTransactionOneBatchWeight();
                u1 = u1 + excelVO.getUCallculationRealSumMoney();
                v1 = v1 + excelVO.getVCallculationRealRestMoney();
                w1 = w1 + excelVO.getWPrintEndContractMoney();

                //累加倒数第二行结果
                o2 = o2 + excelVO.getOForeseenOneBatchWeight();
            }
        }

        /**
         * 1. 根据用户名归档
         * 2. 同样的用户名中，再按时间排序
         */
//        log.info("tempList.size() = " + tempList.size());
        Map<String, List<PrintJobExcelVO>> collect = tempList.stream().collect(Collectors.groupingBy(PrintJobExcelVO::getBCustomerName));

        collect.forEach((key, list)->{
            int size = list.size();
            for (int i = 0; i < size; i++) {
                PrintJobExcelVO e = list.get(i);
                printJobExcelVOList.add(e);
            }
        });

        //处理倒数第二行信息
        if (printJobExcelVOList.size() != 0){
            PrintJobExcelVO firstObject = printJobExcelVOList.get(0);
            a2 = firstObject.getAPrintBeginContractMoney();
            n2 = firstObject.getNForeseenOneBatchCount();
            q2 = firstObject.getQCallculationAmount();
            s2 = firstObject.getSTransactionOneBatchWeight();
            u2 = firstObject.getUCallculationRealSumMoney();
            v2 = firstObject.getVCallculationRealRestMoney();
            w2 = firstObject.getWPrintEndContractMoney();
        }
        o1 = o2;


        //处理最后2行的数据
        PrintJobExcelVO lastSecondObject = new PrintJobExcelVO();
        lastSecondObject.setAPrintBeginContractMoney(a2);
        lastSecondObject.setBCustomerName("Total under the definite contract");
        lastSecondObject.setCCustomerinnRu("");
        lastSecondObject.setDCustomerKppRu("");
        lastSecondObject.setEContractName("");
        lastSecondObject.setFFrankMachineId("");
        lastSecondObject.setGListNumber("");
        lastSecondObject.setHStartDate("");
        lastSecondObject.setIStartTime("");
        lastSecondObject.setJEndDate("");
        lastSecondObject.setKEndTime("");
        lastSecondObject.setLTaxRegionType("");
        lastSecondObject.setMTaxLabelRu("");
        lastSecondObject.setNForeseenOneBatchCount(n2);
        lastSecondObject.setOForeseenOneBatchWeight(o2);
        lastSecondObject.setPTaxFixedValue("");
        lastSecondObject.setQCallculationAmount(q2);
        lastSecondObject.setRTransactionOneBatchCount("");
        lastSecondObject.setSTransactionOneBatchWeight(s2);
        lastSecondObject.setTTaxRealSaleRate("");
        lastSecondObject.setUCallculationRealSumMoney(u2);
        lastSecondObject.setVCallculationRealRestMoney(v2);
        lastSecondObject.setWPrintEndContractMoney(w2);
        lastSecondObject.setCode("");
        lastSecondObject.setStartMsgId(0L);

        //处理最后1行的数据
        PrintJobExcelVO lastOneObject = new PrintJobExcelVO();
        lastOneObject.setAPrintBeginContractMoney(a1);
        lastOneObject.setBCustomerName("Total amount of money for all contracts of the organiztions for the reporting period");
        lastOneObject.setCCustomerinnRu("");
        lastOneObject.setDCustomerKppRu("");
        lastOneObject.setEContractName("");
        lastOneObject.setFFrankMachineId("");
        lastOneObject.setGListNumber("");
        lastOneObject.setHStartDate("");
        lastOneObject.setIStartTime("");
        lastOneObject.setJEndDate("");
        lastOneObject.setKEndTime("");
        lastOneObject.setLTaxRegionType("");
        lastOneObject.setMTaxLabelRu("");
        lastOneObject.setNForeseenOneBatchCount(n1);
        lastOneObject.setOForeseenOneBatchWeight(o1);
        lastOneObject.setPTaxFixedValue("");
        lastOneObject.setQCallculationAmount(q1);
        lastOneObject.setRTransactionOneBatchCount("");
        lastOneObject.setSTransactionOneBatchWeight(s1);
        lastOneObject.setTTaxRealSaleRate("");
        lastOneObject.setUCallculationRealSumMoney(u1);
        lastOneObject.setVCallculationRealRestMoney(v1);
        lastOneObject.setWPrintEndContractMoney(w1);
        lastOneObject.setCode("");
        lastOneObject.setStartMsgId(0L);

        //添加最后三行数据
        PrintJobExcelVO emptyObject = new PrintJobExcelVO();
        printJobExcelVOList.add(emptyObject);
        printJobExcelVOList.add(lastSecondObject);
        printJobExcelVOList.add(lastOneObject);

        return printJobExcelVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPrintJob(PrintJob printJob) {
        this.save(printJob);
    }

    /**
     * 创建前端打印订单
     *
     * @param printJobReq
     */
    @Override
    public void createPrintJobDto(PrintJobReq printJobReq) {
        ArrayList<ForeseenProductFmDto> products = printJobReq.getProducts();

        Contract dbContract = checkPrintJob(printJobReq, products);

        //todo 确定产品编号是否正常


        //确定上一个订单是否闭环
        PrintJob lastestJob = getLastestJobByFmId(printJobReq.getFrankMachineId(), PrintJobTypeEnum.Web.getCode());
        if (lastestJob != null && lastestJob.getFlow() != FlowEnum.FlowEnd.getCode()) {
            throw new FebsException(messageUtils.getMessage("printJob.waitLastOrderFinish"));
        }


        PrintJob printJob = new PrintJob();
        BeanUtils.copyProperties(printJobReq, printJob);
        printJob.setPcUserId(FebsUtil.getCurrentUser().getUserId().intValue());
        printJob.setFlow(FlowEnum.FlowIng.getCode());
        printJob.setFlowDetail(FlowDetailEnum.JobingPcCreatePrint.getCode());
        printJob.setType(PrintJobTypeEnum.Web.getCode());
        printJob.setCreatedTime(new Date());
        //为了报表信息，添加创建打印任务时的开始金额
        printJob.setContractCurrent(dbContract.getCurrent());
        printJob.setContractConsolidate(dbContract.getConsolidate());

        this.save(printJob);

        //log.info(printJob.toString());
        List<ForeseenProduct> productList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ForeseenProduct product = new ForeseenProduct();
            BeanUtils.copyProperties(products.get(i), product);
            product.setPrintJobId(printJob.getId());
            product.setCreatedTime(new Date());
            productList.add(product);
            log.info(product.toString());
        }

        foreseenProductService.saveBatch(productList);
    }

    private Contract checkPrintJob(PrintJobReq printJobReq, ArrayList<ForeseenProductFmDto> products) {
        if (printJobReq.getTotalAmount() < 0) {
            throw new FebsException("Total amount can not less 0");
        }

        if (products.size() == 0){
            throw new FebsException(messageUtils.getMessage("printJob.fillProductInfo"));
        }

        //判断字符长度
        int totalCount = 0;
        for (int i = 0; i < products.size(); i++) {
            ForeseenProductFmDto temp = products.get(i);
            if (temp.getAddress().length() <=1 ){
                throw new FebsException("address length is too short");
            }

            if (temp.getCount() < 0) {
                throw new FebsException("product count can not less 0");
            }

            if (temp.getWeight() < 0) {
                throw new FebsException("product weight can not less 0");
            }

            totalCount += temp.getCount();
        }

        printJobReq.setTotalCount(totalCount);

        //确定合同号是否正常
        Contract dbContract = contractService.getByConractCode(printJobReq.getContractCode());
        if(dbContract == null){
            throw new FebsException("Contract not found");
        }

        //确定机器ID是否正常
        if(!deviceService.checkExistByFmId(printJobReq.getFrankMachineId())){
            throw new FebsException("FrankMachineId not found");
        }
        return dbContract;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePrintJob(PrintJob printJob) {
        this.saveOrUpdate(printJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editAndUpdatePrintJob(PrintJob printJob) {
        LambdaQueryWrapper<PrintJob> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PrintJob::getId,printJob.getId());
        boolean result = this.update(printJob,queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPrintJob(PrintJobReq printJobUpdateDto) {
        PrintJob printJob = new PrintJob();
        ArrayList<ForeseenProductFmDto> products = printJobUpdateDto.getProducts();
        Contract dbContract = checkPrintJob(printJobUpdateDto, products);
        BeanUtils.copyProperties(printJobUpdateDto, printJob);
        printJob.setFlow(FlowEnum.FlowIng.getCode());
        printJob.setFlowDetail(FlowDetailEnum.JobingPcCreatePrint.getCode());
        printJob.setType(PrintJobTypeEnum.Web.getCode());
        printJob.setCreatedTime(new Date());
        //根据业务不能删除所有product
        //通过printJobId更新PrintJob
        this.editAndUpdatePrintJob(printJob);

        //此编辑通过删除原有数据，新增新数据实现，会导致编辑后的唯一id改变

        //删除库中的products
        LambdaQueryWrapper<ForeseenProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ForeseenProduct::getPrintJobId,printJob.getId());
        Boolean delete = foreseenProductService.remove(queryWrapper);
        //处理前端获取的products
        List<ForeseenProduct> productList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ForeseenProduct product = new ForeseenProduct();
            if(null!=products.get(i).getProductCode()) {
                BeanUtils.copyProperties(products.get(i), product);
                product.setPrintJobId(printJob.getId());
                product.setCreatedTime(new Date());
                productList.add(product);
            }
        }
        foreseenProductService.saveOrUpdateBatch(productList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePrintJob(PrintJob printJob) {
        LambdaQueryWrapper<PrintJob> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }

    /**
     * 通过frankMachineId找到没有闭环的打印任务
     *
     * @param frankMachineId
     * @return
     */
    @Override
    public PrintJob getUnFinishJobByFmId(String frankMachineId) {
        LambdaQueryWrapper<PrintJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PrintJob::getFrankMachineId, frankMachineId);
        wrapper.eq(PrintJob::getFlow, FlowEnum.FlowIng.getCode());
        if (FebsConstant.IS_TEST_NETTY){
            return null;
        }
        return this.getOne(wrapper);
    }

    @Override
    public PrintJob getLastestJobByFmId(String frankMachineId, int type) {
        LambdaQueryWrapper<PrintJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PrintJob::getFrankMachineId, frankMachineId);
//        wrapper.eq(PrintJob::getType, type);
        wrapper.orderByDesc(PrintJob::getId);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public PrintJob getByPrintJobId(int printJobId) {
        LambdaQueryWrapper<PrintJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PrintJob::getId, printJobId);
        PrintJob printJob = this.getOne(wrapper);
        if (printJob == null) {
            throw new FmException(FMResultEnum.PrintJobIdNoExist.getCode(), "dbPrintJob == null printJobId = " + printJobId);
        }
        return printJob;
    }

    @Override
    public PrintJob getByForeseenId(String foreseenId) {
        LambdaQueryWrapper<PrintJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PrintJob::getForeseenId, foreseenId);
        PrintJob printJob = this.getOne(wrapper);
        if (printJob == null){
            throw new FmException(FMResultEnum.ForeseenIdNoExist.getCode(), "dbPrintJob == null fmForeseenId="+foreseenId);
        }
        return printJob;
    }

    @Override
    public PrintJob getByTransactionId(String transactionId) {
        LambdaQueryWrapper<PrintJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PrintJob::getTransactionId, transactionId);
        PrintJob printJob = this.getOne(wrapper);
        if (printJob == null){
            throw new FmException(FMResultEnum.TransactionIdNoExist.getCode(), "dbPrintJob == null fmTransactionId="+transactionId);
        }
        return printJob;
    }

     /**
     * job流程中，foreseens的进度更新
     *   foreseen的balance改变current的值
     *   transaction成功后，改变consolidate的值
     * @param curFlowDetail
     * @param balanceDTO
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public PrintJob changeForeseensStatus(ForeseenFMDTO foreseenFmDto, PrintJob dbPrintJob, FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO) {
        /**
         创建job
         创建foreseens和ForeseenProduct
         */
        //判断订单类型
        PrintJobTypeEnum jobType = PrintJobTypeEnum.getByCode(foreseenFmDto.getPrintJobType());


        /**
         1. 第一次创建和第二次创建Foreseen不一样，第一次创建，都是新建
         2. 第二次创建，是先删除旧的，再新建
         */

        //创建foreseens和ForeseenProduct
        Foreseen foreseen = new Foreseen();
        BeanUtils.copyProperties(foreseenFmDto, foreseen);
        foreseen.setTotalAmmount(MoneyUtils.changeF2Y(foreseenFmDto.getTotalAmmount()));
        foreseen.setForeseenStatus(FlowEnum.FlowEnd.getCode());
        foreseen.setForeseenStatus(1);
        foreseen.setUpdatedTime(new Date());
        foreseen.setCreatedTime(new Date());

        //判断是否是机器订单
        boolean isMachineOrder = jobType == PrintJobTypeEnum.Machine;

        String dbForeseenId = "";
        Integer printJobId = 0;
        if (!isMachineOrder){
            printJobId = dbPrintJob.getId();
            dbForeseenId = dbPrintJob.getForeseenId();
            //不是第一次创建Foreseen，先删除旧的，再新建
            if (!StringUtils.isEmpty(dbForeseenId)) {
                Foreseen deleteForeseen = new Foreseen();
                deleteForeseen.setId(dbForeseenId);
                foreseenService.deleteForeseen(deleteForeseen);
            }
            ForeseenProduct deleteProduct = new ForeseenProduct();
            deleteProduct.setPrintJobId(printJobId);
            foreseenProductService.deleteForeseenProduct(deleteProduct);
        }

        foreseenService.createForeseen(foreseen);

        //添加订单产品信息
        List<ForeseenProduct> foreseenProductList = new ArrayList<>();
        if (foreseenFmDto.getProducts() != null) {
            for (ForeseenProductFmDto productDto : foreseenFmDto.getProducts()) {
                ForeseenProduct foreseenProduct = new ForeseenProduct();
                BeanUtils.copyProperties(productDto, foreseenProduct);
                foreseenProduct.setForeseenId(foreseen.getId());
                foreseenProduct.setCreatedTime(new Date());
                foreseenProductList.add(foreseenProduct);
            }
            foreseenProductService.saveBatch(foreseenProductList);
        }

        //创建job
        PrintJob printJob = dbPrintJob == null ? new PrintJob() : dbPrintJob;
        if (curFlowDetail == FlowDetailEnum.JobingForeseensSuccess || curFlowDetail == FlowDetailEnum.JobingErrorForeseensUnKnow) {
            log.info("curFlowDetail == FlowDetailEnum.JobingForeseensSuccess || curFlowDetail == FlowDetailEnum.JobingForeseensUnKnow");
            printJob.setFlow(FlowEnum.FlowIng.getCode());
        } else {
            log.info("curFlowDetail == FlowEnum.FlowEnd.getCode");
            printJob.setFlow(FlowEnum.FlowEnd.getCode());
        }

        printJob.setContractCode(foreseenFmDto.getContractCode());
        printJob.setForeseenId(foreseenFmDto.getId());
        printJob.setTransactionId("");//暂无
        printJob.setUserId(foreseenFmDto.getUserId());
        printJob.setFrankMachineId(foreseenFmDto.getFrankMachineId());
        printJob.setFlowDetail(curFlowDetail.getCode());
        printJob.setUpdatedTime(new Date());




        //机器订单时创建，网络订单是更新
        if (isMachineOrder){
            //机器需要传，网络订单创建的时候已经保存好了
            Contract dbContract = contractService.getByConractCode(foreseenFmDto.getContractCode());
            printJob.setContractCurrent(dbContract.getCurrent());
            printJob.setContractConsolidate(dbContract.getConsolidate());

            //机器需要传，网络订单创建的时候已经保存好了
            printJob.setTotalCount(foreseen.getTotalCount());
            printJob.setTotalAmount(foreseen.getTotalAmmount());
            printJob.setCreatedTime(new Date());
            printJob.setType(PrintJobTypeEnum.Machine.getCode());
            this.createPrintJob(printJob);
        }else{
            printJob.setType(PrintJobTypeEnum.Web.getCode());
            printJob.setId(printJobId);
            this.updateById(printJob);
        }

        //修改合同的申请金额管理
        /**
         * 申请foreseen通过了，那就需要更新合同金额 current做减法
         * 然后取消foreseen后，也需要把那笔金额给退回去
         * transaction以实际消耗金额为主，申请金额也要改成这个
         */
        if (curFlowDetail == FlowDetailEnum.JobingForeseensSuccess) {
            balanceService.saveReturnBalance(foreseenFmDto.getContractCode(), balanceDTO);
        }

        log.info("printJob = " + printJob.toString());
        return printJob;
    }

    /**
     * job流程中，foreseensCancel的进度更新
     * foreseen的balance改变current的值
     * transaction成功后，改变consolidate的值
     * @param cancelJobFMDTO
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeForeseensCancelStatus(PrintJob dbPrintJob, CancelJobFMDTO cancelJobFMDTO, FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO) {
        //更新PrintJob
        if (curFlowDetail == FlowDetailEnum.JobingErrorForeseensCancelUnKnow) {
            dbPrintJob.setFlow(FlowEnum.FlowIng.getCode());
        } else if (curFlowDetail == FlowDetailEnum.JobingErrorForeseensCancel4xx) {
            dbPrintJob.setFlow(FlowEnum.FlowIng.getCode());
        } else {
            dbPrintJob.setFlow(FlowEnum.FlowEnd.getCode());
            balanceService.saveReturnBalance(dbPrintJob.getContractCode(), balanceDTO);
            /*Contract dbContract = contractService.getByConractCode(dbPrintJob.getContractCode());
            Double dbCurrent = dbContract.getCurrent();

            //foreseen的金额 关联current
            Foreseen dbForeseen = foreseenService.getById(dbPrintJob.getForeseenId());
            Double userdCurrent = dbForeseen.getTotalAmmount();

            double newCurrent = DoubleKit.add(dbCurrent, userdCurrent);
            dbContract.setCurrent(newCurrent);
            contractService.saveOrUpdate(dbContract);
            log.info("取消订单后：dbCurrent={}, userdCurrent={}, newCurrent={}", dbCurrent, userdCurrent, newCurrent);*/
        }
        dbPrintJob.setUpdatedTime(new Date());
        dbPrintJob.setFlowDetail(curFlowDetail.getCode());
        dbPrintJob.setCancelMsgCode(cancelJobFMDTO.getCancelMsgCode());
        this.updatePrintJob(dbPrintJob);

        if (dbPrintJob.getType() == PrintJobTypeEnum.Web.getCode()) {
            noticeFrontService.notice(7, messageUtils.getMessage("printJob.machineCancel"), dbPrintJob.getPcUserId());
        }
    }

    /**
     * job流程中，Transaction的进度更新
     * foreseen的balance改变current的值
     * transaction成功后，改变consolidate的值
     * @param dbPrintJob
     * @param transactionDTO
     * @param curFlowDetail
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public Contract changeTransactionStatus(PrintJob dbPrintJob, Contract dbContract, TransactionDTO transactionDTO, FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO) {
        dbPrintJob.setFlowDetail(curFlowDetail.getCode());
        //更新PrintJob
        if (curFlowDetail == FlowDetailEnum.JobEndSuccess) {
            dbPrintJob.setFlow(FlowEnum.FlowEnd.getCode());
            dbPrintJob.setUpdatedTime(new Date());
//            dbPrintJob.setTransactionId(transactionDTO.getId());
            this.updatePrintJob(dbPrintJob);
        } else {
            //碰到这种异常，保存进度，不保存transaction 和 frank 不修改金额
            dbPrintJob.setFlow(FlowEnum.FlowIng.getCode());
            dbPrintJob.setUpdatedTime(new Date());
            this.updatePrintJob(dbPrintJob);
            return dbContract;
        }


        //完成 transaction
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO, transaction);
        transaction.setTransactionStatus(FlowEnum.FlowEnd.getCode());
        transaction.setStopDateTime(DateKit.createRussiatime(new Date()));
        transaction.setUpdatedTime(new Date());
        transactionService.updateTransaction(transaction);

        //如果一切OK
        if (curFlowDetail == FlowDetailEnum.JobEndSuccess) {
            balanceService.saveReturnBalance(balanceDTO.getContractCode(), balanceDTO);
            dbContract.setConsolidate(balanceDTO.getConsolidate());
            dbContract.setCurrent(balanceDTO.getCurrent());

            if (dbPrintJob.getType() == PrintJobTypeEnum.Web.getCode()) {
                noticeFrontService.notice(8, messageUtils.getMessage("printJob.printSuccess"), dbPrintJob.getPcUserId());
            }
        }else{
            if (dbPrintJob.getType() == PrintJobTypeEnum.Web.getCode()) {
                noticeFrontService.notice(8, messageUtils.getMessage("printJob.printAbnormal"), dbPrintJob.getPcUserId());
            }
        }

        //返回最新的contract
        return dbContract;
    }

    @Override
    public Foreseen getForeseenById(String foreseenId) {
        return foreseenService.getById(foreseenId);
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        return transactionService.getById(transactionId);
    }

    /**
     * 返回true，表示这个机器的所有打印任务都完成了
     * @param frankMachineId
     * @return
     */
    @Override
    public boolean checkPrintJobFinish(String frankMachineId) {
        LambdaQueryWrapper<PrintJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PrintJob::getFrankMachineId, frankMachineId);
        wrapper.eq(PrintJob::getFlow, FlowEnum.FlowIng.getCode());
        Integer unFinishCount = this.baseMapper.selectCount(wrapper);

        return unFinishCount == 0;
    }

    /**
     * 处理打印，可能有多种情况
     *
     * @param printJobId
     */
    @Override
    public void doPrintJob(int printJobId) {
        log.info("开始处理打印");

        PrintJob dbPrintJob = getByPrintJobId(printJobId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbPrintJob.getFlow());

        //判断打印是否完成
        if (dbFlow == FlowEnum.FlowEnd){
            throw new FebsException(messageUtils.getMessage("printJob.noRepeatClick"));
        }

        //todo 俄罗斯新的修改需求  判断是否有广告图片，而且这个图片是否下载成功，没有则报错，让更新图片列表给机器，让机器重新下载

        serviceToMachineProtocol.doPrintJob(dbPrintJob);
    }

    /**
     * 开始取消打印任务
     *
     * @param id
     */
    @Override
    public void cancelPrintJob(int id) {
        try {
            PrintJob dbPrintJob = getByPrintJobId(id);
            FlowDetailEnum dbFlowDetail = FlowDetailEnum.getByCode(dbPrintJob.getFlowDetail());
            FlowEnum dbFlow = FlowEnum.getByCode(dbPrintJob.getFlow());

            if (dbFlow == FlowEnum.FlowEnd){
                throw new FebsException(messageUtils.getMessage("printJob.noCancelPrintJob"));
            }

            //哪些情况可以点击取消打印呢？没有开始transaction的时候
            boolean enableCancle = true;
            if (dbFlowDetail == FlowDetailEnum.JobingErrorTransactionUnKnow
                    || dbFlowDetail == FlowDetailEnum.JobingErrorTransaction4xx){
                enableCancle = false;
            }

            //判断是否可以取消打印任务
            if (!enableCancle){
                throw new FebsException(messageUtils.getMessage("printJob.waitCancelPrintJob"));
            }

            serviceToMachineProtocol.cancelPrintJob(dbPrintJob);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        }
    }

    /**
     * 获取PC上的订单的信息
     *
     * @param dbPrintJob
     * @return
     */
    @Override
    public PrintProgressInfo getProductPrintProgress(PrintJob dbPrintJob) {
        Integer printJobId = dbPrintJob.getId();
        String frankMachineId = dbPrintJob.getFrankMachineId();
        String transactionId = dbPrintJob.getTransactionId();

        PrintProgressInfo printProgressInfo = new PrintProgressInfo();
        printProgressInfo.setActualAmount("0");
        printProgressInfo.setActualCount(0);

        //构建信息
        ForeseenProduct product = new ForeseenProduct();
        product.setPrintJobId(printJobId);
        List<ForeseenProduct> productList = foreseenProductService.findForeseenProducts(product);

        //产品信息和进度
        HashMap<String, Integer> productCountMap = null;

        //得到每个产品的进度
        if (!StringUtils.isEmpty(transactionId)){
            DmMsgDetail dmMsgDetail = transactionMsgService.getDmMsgDetailAfterFinishJob(transactionId, true);
            productCountMap = dmMsgDetail.getProductCountMap();
            printProgressInfo.setActualAmount(dmMsgDetail.getActualAmount());
            printProgressInfo.setActualCount(dmMsgDetail.getActualCount());
        }

        //拼接产品进度
        ForeseenProductFmDto[] productArr = new ForeseenProductFmDto[productList.size()];


        for (int i = 0; i < productList.size(); i++) {
            ForeseenProductFmDto temp = new ForeseenProductFmDto();
            BeanUtils.copyProperties(productList.get(i), temp);
            temp.setAlreadyPrintCount(0);
            if (productCountMap != null && productCountMap.size() > 0){
                Integer alreadyPrintCount = productCountMap.get(temp.getProductCode());
                temp.setAlreadyPrintCount(alreadyPrintCount == null ? 0 : alreadyPrintCount);
            }
//            log.info("temp = " + temp.toString());
            productArr[i] = temp;
        }
        printProgressInfo.setProductArr(productArr);

        return printProgressInfo;
    }
}
