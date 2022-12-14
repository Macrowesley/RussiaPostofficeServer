package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.entity.RoleType;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.license.LicenseVerifyUtils;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFmReqDTO;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.*;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.common.kit.StringKit;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.dto.machine.PrintProgressInfo;
import cc.mrbird.febs.rcs.dto.manager.*;
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
 * ??????????????? Service??????
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
    LicenseVerifyUtils verifyUtils;

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

        //printJobDto.getFlow()?????????0???1???2???3
        //0???notBegin,1???flowing,2???successFlowed,3???failFlowed
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

        //???????????????????????????????????????????????????
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
                //??????????????????????????????????????????????????????????????????????????????
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
     * ??????excel???????????????
     *
     * @param queryRequest
     * @param printJob
     * @return
     */
    @Override
    public List<PrintJobExcelVO> selectExcelData(PrintJobDTO printJobDto) {
        log.info("excel???????????????printJobDto = " + printJobDto.toString());
        List<PrintJob> printJobList = findPrintJobs(printJobDto);
        int printJobSize = printJobList.size();
        log.info("printJobList.size = " + printJobSize);

        ArrayList<PrintJobExcelVO> tempList = new ArrayList<>();
        ArrayList<PrintJobExcelVO> printJobExcelVOList = new ArrayList<>();


        //?????????????????????
        double a2 = 0,q2 = 0,u2 = 0,v2 = 0,w2 = 0;
        int n2 = 0,o2 = 0,s2 = 0;
        //?????????1?????????
        double a1 = 0,q1 = 0,u1 = 0,v1 = 0,w1 = 0;
        int n1 = 0,o1 = 0,s1 = 0;


        for (int m = 0; m < printJobSize; m++) {
            PrintJob  bean = printJobList.get(m);
//            log.info("/n ============");
//            log.info("?????????????????????" + bean.toString());

            //?????????????????????????????????
            long t1 = System.currentTimeMillis();
            List<TransactionMsgBatchInfo> onePrintJobTransactionProuductList = transactionMsgService.mergeMsgList(bean.getTransactionId());
//            log.info("??????????????????????????????????????????{}",(System.currentTimeMillis() -t1));

            //???????????????????????????
            ContractCustomerInfo customerInfo = contractService.findContractAndCustomer(bean.getContractCode());
//            log.info(customerInfo.toString());

            int size = onePrintJobTransactionProuductList.size();
            for (int i = 0; i < size; i++) {
                //?????????????????????
                TransactionMsgBatchInfo batchBean = onePrintJobTransactionProuductList.get(i);
                log.info("??????????????????????????????batchBean = " + batchBean.toString());
                PrintJobExcelVO excelVO = new PrintJobExcelVO();
                //printjob?????????????????????????????????
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

                //??????????????????
                excelVO.setCode(batchBean.getCode());
                excelVO.setStartMsgId(batchBean.getStartMsgId());
                tempList.add(excelVO);
                log.info("Excel???????????? excelVO = " + excelVO.toString());

                //??????????????????????????????
                a1 = a1 + excelVO.getAPrintBeginContractMoney();
                n1 = n1 + excelVO.getNForeseenOneBatchCount();
                q1 = q1 + excelVO.getQCallculationAmount();
                s1 = s1 + excelVO.getSTransactionOneBatchWeight();
                u1 = u1 + excelVO.getUCallculationRealSumMoney();
                v1 = v1 + excelVO.getVCallculationRealRestMoney();
                w1 = w1 + excelVO.getWPrintEndContractMoney();

                //???????????????????????????
                o2 = o2 + excelVO.getOForeseenOneBatchWeight();
            }
        }

        /**
         * 1. ?????????????????????
         * 2. ??????????????????????????????????????????
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

        //???????????????????????????
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


        //????????????2????????????
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

        //????????????1????????????
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

        //????????????????????????
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
     * ????????????????????????
     *
     * @param printJobReq
     */
    @Transactional(rollbackFor = FebsException.class)
    @Override
    public FebsResponse createPrintJobDto(PrintJobReq printJobReq) {

        if (!verifyUtils.verify()) {
            throw new FebsException(messageUtils.getMessage("printJob.expiredLicense"));
        }
        log.info("?????????????????????" + printJobReq.toString());



        List<ForeseenProductPcReqDTO> products = printJobReq.getProducts();

        Contract dbContract = checkPrintJob(printJobReq, products);

        //todo ??????????????????????????????


        //?????????????????????????????????
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
        //???????????????????????????????????????????????????????????????
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
            String address = product.getAddress();
            if (product.getType() == 1 && StringUtils.isNotBlank(address)){
                int maxLen = 120;
                if (address.length() > maxLen) {
                    throw new FebsException("The length of the address cannot exceed " + maxLen + " characters\n");
                }
                //????????????
                product.setAddress(StringKit.splitString(address,30));
            }
            log.info(product.toString());
        }

        foreseenProductService.saveBatch(productList);

        return new FebsResponse().success();
    }

    private Contract checkPrintJob(PrintJobReq printJobReq, List<ForeseenProductPcReqDTO> products) {
        if (printJobReq.getTotalAmount() < 0) {
            throw new FebsException("Total amount can not less 0");
        }

        if (products == null || products.size() == 0){
            throw new FebsException(messageUtils.getMessage("printJob.fillProductInfo"));
        }

        //??????????????????
        int totalCount = 0;
        for (int i = 0; i < products.size(); i++) {
            ForeseenProductPcReqDTO temp = products.get(i);
            /*if (temp.getAddress().length() <=1 ){
                throw new FebsException("address length is too short");
            }
*/
            if (temp.getCount() < 0) {
                throw new FebsException("product count can not less 0");
            }

            if (temp.getWeight() < 0) {
                throw new FebsException("product weight can not less 0");
            }

            totalCount += temp.getCount();
        }

        printJobReq.setTotalCount(totalCount);

        //???????????????????????????
        Contract dbContract = contractService.getByConractCode(printJobReq.getContractCode());
        if(dbContract == null){
            throw new FebsException("Contract not found");
        }

        //????????????ID????????????
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
    public FebsResponse editPrintJob(PrintJobReq printJobUpdateDto) {
        PrintJob printJob = new PrintJob();
        List<ForeseenProductPcReqDTO> products = printJobUpdateDto.getProducts();
        Contract dbContract = checkPrintJob(printJobUpdateDto, products);
        BeanUtils.copyProperties(printJobUpdateDto, printJob);
        printJob.setFlow(FlowEnum.FlowIng.getCode());
        printJob.setFlowDetail(FlowDetailEnum.JobingPcCreatePrint.getCode());
        printJob.setType(PrintJobTypeEnum.Web.getCode());
        printJob.setCreatedTime(new Date());
        //??????????????????????????????product
        //??????printJobId??????PrintJob
        this.editAndUpdatePrintJob(printJob);

        //???????????????????????????????????????????????????????????????????????????????????????id??????

        //???????????????products
        LambdaQueryWrapper<ForeseenProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ForeseenProduct::getPrintJobId,printJob.getId());
        Boolean delete = foreseenProductService.remove(queryWrapper);
        //?????????????????????products
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
        return new FebsResponse().success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePrintJob(PrintJob printJob) {
        LambdaQueryWrapper<PrintJob> wrapper = new LambdaQueryWrapper<>();
        // TODO ??????????????????
        this.remove(wrapper);
    }

    /**
     * ??????frankMachineId?????????????????????????????????
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
     * job????????????foreseens???????????????
     *   foreseen???balance??????current??????
     *   transaction??????????????????consolidate??????
     * @param curFlowDetail
     * @param balanceDTO
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public PrintJob changeForeseensStatus(ForeseenFmReqDTO foreseenFmReqDTO, PrintJob dbPrintJob, FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO) {
        /**
         ??????job
         ??????foreseens???ForeseenProduct
         */
        //??????????????????
        PrintJobTypeEnum jobType = PrintJobTypeEnum.getByCode(foreseenFmReqDTO.getPrintJobType());


        /**
         1. ?????????????????????????????????Foreseen??????????????????????????????????????????
         2. ????????????????????????????????????????????????
         */
        //??????foreseens???ForeseenProduct
        Foreseen foreseen = new Foreseen();
        BeanUtils.copyProperties(foreseenFmReqDTO, foreseen);
        foreseen.setTotalAmmount(MoneyUtils.changeF2Y(foreseenFmReqDTO.getTotalAmmount()));
        foreseen.setForeseenStatus(FlowEnum.FlowEnd.getCode());
        foreseen.setForeseenStatus(1);
        foreseen.setUpdatedTime(new Date());

        //???????????????????????????
        boolean isMachineOrder = jobType == PrintJobTypeEnum.Machine;

        if (!isMachineOrder){
            String dbForeseenId = dbPrintJob.getForeseenId();
            //?????????????????????Foreseen??????????????????????????????
            if (!StringUtils.isEmpty(dbForeseenId)) {
                foreseen.setId(dbForeseenId);
                foreseenService.updateForeseen(foreseen);
            }else{
                foreseen.setCreatedTime(new Date());
                foreseenService.createForeseen(foreseen);
            }
        }else{
            foreseen.setCreatedTime(new Date());
            foreseenService.createForeseen(foreseen);
        }


        //???????????? ???????????????????????? ???PC???????????????/???????????????????????????????????????????????????
        List<ForeseenProduct> foreseenProductList = new ArrayList<>();
        if (foreseenFmReqDTO.getProducts() != null && isMachineOrder) {
            for (ForeseenProductFmReqDTO productDto : foreseenFmReqDTO.getProducts()) {
                ForeseenProduct bean = new ForeseenProduct();
                BeanUtils.copyProperties(productDto, bean);
                bean.setType(ForeseenProductTypeEnum.MACHINE_ADDRESS.getCode());
                bean.setCreatedTime(new Date());
                foreseenProductList.add(bean);
            }
            foreseenProductService.saveBatch(foreseenProductList);
        }

        //??????job
        PrintJob printJob = dbPrintJob == null ? new PrintJob() : dbPrintJob;
        if (curFlowDetail == FlowDetailEnum.JobingForeseensSuccess || curFlowDetail == FlowDetailEnum.JobingErrorForeseensUnKnow) {
            log.info("curFlowDetail == FlowDetailEnum.JobingForeseensSuccess || curFlowDetail == FlowDetailEnum.JobingForeseensUnKnow");
            printJob.setFlow(FlowEnum.FlowIng.getCode());
        } else {
            log.info("curFlowDetail == FlowEnum.FlowEnd.getCode");
            printJob.setFlow(FlowEnum.FlowEnd.getCode());
        }

        printJob.setContractCode(foreseenFmReqDTO.getContractCode());
        printJob.setForeseenId(foreseenFmReqDTO.getId());
        printJob.setTransactionId("");//??????
        printJob.setUserId(foreseenFmReqDTO.getUserId());
        printJob.setFrankMachineId(foreseenFmReqDTO.getFrankMachineId());
        printJob.setFlowDetail(curFlowDetail.getCode());
        printJob.setUpdatedTime(new Date());

        //?????????????????????????????????????????????
        if (isMachineOrder){
            //???????????????????????????????????????????????????????????????
            Contract dbContract = contractService.getByConractCode(foreseenFmReqDTO.getContractCode());
            printJob.setContractCurrent(dbContract.getCurrent());
            printJob.setContractConsolidate(dbContract.getConsolidate());

            //???????????????????????????????????????????????????????????????
            printJob.setTotalCount(foreseen.getTotalCount());
            printJob.setTotalAmount(foreseen.getTotalAmmount());
            printJob.setCreatedTime(new Date());
            printJob.setType(PrintJobTypeEnum.Machine.getCode());
            this.createPrintJob(printJob);
        }else{
            printJob.setType(PrintJobTypeEnum.Web.getCode());
            printJob.setId(dbPrintJob.getId());
            this.updateById(printJob);
        }

        //?????????????????????????????????
        /**
         * ??????foreseen?????????????????????????????????????????? current?????????
         * ????????????foreseen??????????????????????????????????????????
         * transaction????????????????????????????????????????????????????????????
         */
        if (curFlowDetail == FlowDetailEnum.JobingForeseensSuccess) {
            balanceService.saveReturnBalance(foreseenFmReqDTO.getContractCode(), balanceDTO);
        }

        log.info("printJob = " + printJob.toString());
        return printJob;
    }

    /**
     * job????????????foreseensCancel???????????????
     * foreseen???balance??????current??????
     * transaction??????????????????consolidate??????
     * @param cancelJobFMDTO
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeForeseensCancelStatus(PrintJob dbPrintJob, CancelJobFMDTO cancelJobFMDTO, FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO) {
        //??????PrintJob
        if (curFlowDetail == FlowDetailEnum.JobingErrorForeseensCancelUnKnow) {
            dbPrintJob.setFlow(FlowEnum.FlowIng.getCode());
        } else if (curFlowDetail == FlowDetailEnum.JobingErrorForeseensCancel4xx) {
            dbPrintJob.setFlow(FlowEnum.FlowIng.getCode());
        } else {
            dbPrintJob.setFlow(FlowEnum.FlowEnd.getCode());
            balanceService.saveReturnBalance(dbPrintJob.getContractCode(), balanceDTO);
            /*Contract dbContract = contractService.getByConractCode(dbPrintJob.getContractCode());
            Double dbCurrent = dbContract.getCurrent();

            //foreseen????????? ??????current
            Foreseen dbForeseen = foreseenService.getById(dbPrintJob.getForeseenId());
            Double userdCurrent = dbForeseen.getTotalAmmount();

            double newCurrent = DoubleKit.add(dbCurrent, userdCurrent);
            dbContract.setCurrent(newCurrent);
            contractService.saveOrUpdate(dbContract);
            log.info("??????????????????dbCurrent={}, userdCurrent={}, newCurrent={}", dbCurrent, userdCurrent, newCurrent);*/
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
     * job????????????Transaction???????????????
     * foreseen???balance??????current??????
     * transaction??????????????????consolidate??????
     * @param dbPrintJob
     * @param transactionDTO
     * @param curFlowDetail
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public Contract changeTransactionStatus(PrintJob dbPrintJob, Contract dbContract, TransactionDTO transactionDTO, FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO) {
        dbPrintJob.setFlowDetail(curFlowDetail.getCode());
        //??????PrintJob
        if (curFlowDetail == FlowDetailEnum.JobEndSuccess) {
            dbPrintJob.setFlow(FlowEnum.FlowEnd.getCode());
            dbPrintJob.setUpdatedTime(new Date());
//            dbPrintJob.setTransactionId(transactionDTO.getId());
            this.updatePrintJob(dbPrintJob);
        } else {
            //?????????????????????????????????????????????transaction ??? frank ???????????????
            dbPrintJob.setFlow(FlowEnum.FlowIng.getCode());
            dbPrintJob.setUpdatedTime(new Date());
            this.updatePrintJob(dbPrintJob);
            return dbContract;
        }


        //?????? transaction
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO, transaction);
        transaction.setTransactionStatus(FlowEnum.FlowEnd.getCode());
        transaction.setStopDateTime(DateKit.createRussiatime(new Date()));
        transaction.setUpdatedTime(new Date());
        transactionService.updateTransaction(transaction);

        //????????????OK
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

        //???????????????contract
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
     * ??????true??????????????????????????????????????????????????????
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
     * ????????????????????????????????????
     *
     * @param printJobId
     */
    @Override
    public void doPrintJob(int printJobId) {
        log.info("??????????????????");

        PrintJob dbPrintJob = getByPrintJobId(printJobId);

        FlowEnum dbFlow = FlowEnum.getByCode(dbPrintJob.getFlow());

        //????????????????????????
        if (dbFlow == FlowEnum.FlowEnd){
            throw new FebsException(messageUtils.getMessage("printJob.noRepeatClick"));
        }

        //todo ???????????????????????????  ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????

        serviceToMachineProtocol.doPrintJob(dbPrintJob);
    }

    /**
     * ????????????????????????
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

            //??????????????????????????????????????????????????????transaction?????????
            boolean enableCancle = true;
            if (dbFlowDetail == FlowDetailEnum.JobingErrorTransactionUnKnow
                    || dbFlowDetail == FlowDetailEnum.JobingErrorTransaction4xx){
                enableCancle = false;
            }

            //????????????????????????????????????
            if (!enableCancle){
                throw new FebsException(messageUtils.getMessage("printJob.waitCancelPrintJob"));
            }

            serviceToMachineProtocol.cancelPrintJob(dbPrintJob);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        }
    }

    /**
     * ??????PC?????????????????????
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



        //?????????????????????
        HashMap<String, Integer> productCountMap = null;

        //???????????????????????????
        if (!StringUtils.isEmpty(transactionId)){
            DmMsgDetail dmMsgDetail = transactionMsgService.getDmMsgDetailAfterFinishJob(transactionId, true);
            productCountMap = dmMsgDetail.getProductCountMap();
            printProgressInfo.setActualAmount(dmMsgDetail.getActualAmount());
            printProgressInfo.setActualCount(dmMsgDetail.getActualCount());
        }


        //??????????????????
        List<ForeseenProductFmRespDTO> productList = foreseenProductService.selectFmProductAdList(printJobId);


        //??????????????????
        ForeseenProductFmRespDTO[] productArr = new ForeseenProductFmRespDTO[productList.size()];


        for (int i = 0; i < productList.size(); i++) {
            ForeseenProductFmRespDTO temp = new ForeseenProductFmRespDTO();
            BeanUtils.copyProperties(productList.get(i), temp);

            String url = temp.getAdImagePath();
            if(StringUtils.isNotBlank(url)){
                String[] split = url.split("/");
                String fileName = split[split.length -1];
                temp.setAdImageName(fileName);
                temp.setAdImagePath("");
            }

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
