package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.PcPrintInfoDTO;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.enums.PrintJobTypeEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.dto.manager.ForeseenDTO;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductDTO;
import cc.mrbird.febs.rcs.dto.manager.ManagerBalanceDTO;
import cc.mrbird.febs.rcs.dto.manager.TransactionDTO;
import cc.mrbird.febs.rcs.dto.ui.PrintJobAddDto;
import cc.mrbird.febs.rcs.entity.*;
import cc.mrbird.febs.rcs.mapper.PrintJobMapper;
import cc.mrbird.febs.rcs.service.*;
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
import java.util.HashMap;
import java.util.List;

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

    @Override
    public IPage<PrintJob> findPrintJobs(QueryRequest request, PrintJob printJob) {
        LambdaQueryWrapper<PrintJob> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        queryWrapper.orderByDesc(PrintJob::getId);
        Page<PrintJob> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<PrintJob> findPrintJobs(PrintJob printJob) {
        LambdaQueryWrapper<PrintJob> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPrintJob(PrintJob printJob) {
        this.save(printJob);
    }

    /**
     * 创建前端打印订单
     *
     * @param printJobAddDto
     */
    @Override
    public void createPrintJobDto(PrintJobAddDto printJobAddDto) {
        PrintJob printJob = new PrintJob();
        BeanUtils.copyProperties(printJobAddDto, printJob);
        printJob.setFlow(FlowEnum.FlowIng.getCode());
        printJob.setFlowDetail(FlowDetailEnum.JobingPcCreatePrint.getCode());
        printJob.setType(PrintJobTypeEnum.Web.getCode());
        printJob.setCreatedTime(new Date());
        this.save(printJob);

        log.info(printJob.toString());

        ArrayList<ForeseenProductDTO> products = printJobAddDto.getProducts();
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePrintJob(PrintJob printJob) {
        this.saveOrUpdate(printJob);
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
    public PrintJob getLastestJobByFmId(String frankMachineId) {
        LambdaQueryWrapper<PrintJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PrintJob::getFrankMachineId, frankMachineId);
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
     * @param dbDevice
     * @param flowDetailEnum
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeForeseensStatus(ForeseenDTO foreseenDTO, FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO) {
        /**
         创建job
         创建foreseens和ForeseenProduct
         */
        //判断订单类型
        PrintJobTypeEnum jobType = PrintJobTypeEnum.getByCode(foreseenDTO.getPrintJobType());

        Integer printJobId = foreseenDTO.getPrintJobId();
        PrintJob dbPrintJob = null;
        if (printJobId != null && jobType == PrintJobTypeEnum.Web){
            dbPrintJob = getByPrintJobId(printJobId);
        }


        /**
         1. 第一次创建和第二次创建Foreseen不一样，第一次创建，都是新建
         2. 第二次创建，是先删除旧的，再新建
         */

        //创建foreseens和ForeseenProduct
        Foreseen foreseen = new Foreseen();
        BeanUtils.copyProperties(foreseenDTO, foreseen);
        foreseen.setTotalAmmount(foreseenDTO.getTotalAmount());
        foreseen.setForeseenStatus(FlowEnum.FlowEnd.getCode());
        foreseen.setForeseenStatus(1);
        foreseen.setUpdatedTime(new Date());
        foreseen.setCreatedTime(new Date());

        //判断是否是机器订单
        boolean isMachineOrder = jobType == PrintJobTypeEnum.Machine;
        String dbForeseenId = dbPrintJob.getForeseenId();
        if (!isMachineOrder && !StringUtils.isEmpty(dbForeseenId)){
            //不是第一次创建Foreseen，先删除旧的，再新建
            Foreseen deleteForeseen = new Foreseen();
            deleteForeseen.setId(dbForeseenId);
            foreseenService.deleteForeseen(deleteForeseen);

            ForeseenProduct deleteProduct = new ForeseenProduct();
            deleteProduct.setForeseenId(dbForeseenId);
            foreseenProductService.deleteForeseenProduct(deleteProduct);
        }

        foreseenService.createForeseen(foreseen);

        //添加订单产品信息
        List<ForeseenProduct> foreseenProductList = new ArrayList<>();
        if (foreseenDTO.getProducts() != null) {
            for (ForeseenProductDTO productDto : foreseenDTO.getProducts()) {
                ForeseenProduct foreseenProduct = new ForeseenProduct();
                BeanUtils.copyProperties(productDto, foreseenProduct);
                foreseenProduct.setForeseenId(foreseen.getId());
                foreseenProduct.setCreatedTime(new Date());
                foreseenProductList.add(foreseenProduct);
            }
            foreseenProductService.saveBatch(foreseenProductList);
        }

        //创建job
        PrintJob printJob = new PrintJob();
        if (curFlowDetail == FlowDetailEnum.JobingForeseensSuccess || curFlowDetail == FlowDetailEnum.JobingErrorForeseensUnKnow) {
            log.info("curFlowDetail == FlowDetailEnum.JobingForeseensSuccess || curFlowDetail == FlowDetailEnum.JobingForeseensUnKnow");
            printJob.setFlow(FlowEnum.FlowIng.getCode());
        } else {
            log.info("curFlowDetail == FlowEnum.FlowEnd.getCode");
            printJob.setFlow(FlowEnum.FlowEnd.getCode());
        }

        printJob.setContractCode(foreseenDTO.getContractCode());
        printJob.setForeseenId(foreseenDTO.getId());
        printJob.setTransactionId("");//暂无
        printJob.setUserId(foreseenDTO.getUserId());
        printJob.setFrankMachineId(foreseenDTO.getFrankMachineId());
        printJob.setFlowDetail(curFlowDetail.getCode());
        printJob.setUpdatedTime(new Date());
        printJob.setCreatedTime(new Date());

        if (isMachineOrder){
            this.createPrintJob(printJob);
        }else{
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
            balanceService.saveReturnBalance(foreseenDTO.getContractCode(), balanceDTO);
        }
    }

    /**
     * job流程中，foreseensCancel的进度更新
     * foreseen的balance改变current的值
     * transaction成功后，改变consolidate的值
     * @param cancelJobFMDTO
     * @param flowDetailEnum
     */
    @Override
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
        /**
         从数据库找到这个printJob
         判断job的flow是否结束
            结束
                返回已经打印完成了，不能再次打印
            未结束
                说明是某些原因导致的中断，需要重新发送订单信息和进度给机器

         */
        //判断现在的进度
        try {
            PrintJob dbPrintJob = getByPrintJobId(printJobId);

            FlowDetailEnum dbFlowDetail = FlowDetailEnum.getByCode(dbPrintJob.getFlowDetail());
            FlowEnum dbFlow = FlowEnum.getByCode(dbPrintJob.getFlow());

            //判断打印是否完成
            if (dbFlow == FlowEnum.FlowEnd){
                throw new FebsException("打印已经完成，请勿重复点击");
            }



            serviceToMachineProtocol.doPrintJob(dbPrintJob);
        } catch (FebsException e) {
            throw new FebsException(e.getMessage());
        }
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
                throw new FebsException("打印已经完成，不能取消打印任务");
            }

            //哪些情况可以点击取消打印呢？没有开始transaction的时候
            boolean enableCancle = true;
            if (dbFlowDetail == FlowDetailEnum.JobingErrorTransactionUnKnow
                    || dbFlowDetail == FlowDetailEnum.JobingErrorTransaction4xx){
                enableCancle = false;
            }

            //判断是否可以取消打印任务
            if (!enableCancle){
                throw new FebsException("当前状态不能取消打印任务，请稍等");
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
    public PcPrintInfoDTO getPcPrintInfo(PrintJob dbPrintJob) {
        Integer printJobId = dbPrintJob.getId();
        String frankMachineId = dbPrintJob.getFrankMachineId();
        String transactionId = dbPrintJob.getTransactionId();

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
        }

        //拼接产品进度
        ForeseenProductDTO[] productArr = null;
        ForeseenProductDTO temp = new ForeseenProductDTO();

        for (int i = 0; i < productList.size(); i++) {
            BeanUtils.copyProperties(productList.get(i), temp);
            if (productCountMap != null && productCountMap.size() > 0){
                temp.setAlreadyPrintCount(productCountMap.get(temp.getProductCode()));
            }
            productArr[i] = temp;
        }

        PcPrintInfoDTO pcPrintInfoDTO = new PcPrintInfoDTO();
        pcPrintInfoDTO.setPrintJobId(printJobId);
        pcPrintInfoDTO.setFrankMachineId(frankMachineId);
        pcPrintInfoDTO.setForeseenId(dbPrintJob.getForeseenId());
        pcPrintInfoDTO.setTransactionId(transactionId);
        pcPrintInfoDTO.setContractCode(dbPrintJob.getContractCode());
        pcPrintInfoDTO.setFlowDetail(dbPrintJob.getFlowDetail());
        pcPrintInfoDTO.setPrintJobType(dbPrintJob.getType());
        pcPrintInfoDTO.setPrintProducts(productArr);

        return pcPrintInfoDTO;
    }
}
