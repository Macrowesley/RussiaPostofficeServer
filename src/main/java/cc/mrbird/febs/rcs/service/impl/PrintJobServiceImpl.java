package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.manager.ForeseenDTO;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductDTO;
import cc.mrbird.febs.rcs.dto.manager.ManagerBalanceDTO;
import cc.mrbird.febs.rcs.dto.manager.TransactionDTO;
import cc.mrbird.febs.rcs.entity.*;
import cc.mrbird.febs.rcs.mapper.PrintJobMapper;
import cc.mrbird.febs.rcs.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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

        boolean isForeseensSuccess = curFlowDetail == FlowDetailEnum.JobingForeseensSuccess;
        //创建job
        PrintJob printJob = new PrintJob();
        if (isForeseensSuccess) {
            log.info("curFlowDetail == FlowDetailEnum.JobingForeseensSuccess");
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
        this.createPrintJob(printJob);


        //2. 创建foreseens和ForeseenProduct
        Foreseen foreseen = new Foreseen();
        BeanUtils.copyProperties(foreseenDTO, foreseen);
        foreseen.setForeseenStatus(FlowEnum.FlowEnd.getCode());
        foreseen.setForeseenStatus(1);
        foreseen.setUpdatedTime(new Date());
        foreseen.setCreatedTime(new Date());
        foreseenService.createForeseen(foreseen);

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


        //todo 修改合同的申请金额管理
        /**
         * 申请foreseen通过了，那就需要更新合同金额 current做减法
         * 然后取消foreseen后，也需要把那笔金额给退回去
         * transaction以实际消耗金额为主，申请金额也要改成这个
         */
        if (isForeseensSuccess) {
            /*Contract dbContract = contractService.getByConractId(foreseenDTO.getContractId());
            Double consolidate = dbContract.getConsolidate();
            double newConsolidate = DoubleKit.sub(consolidate, foreseenDTO.getTotalAmmount());
            dbContract.setConsolidate(newConsolidate);
            contractService.saveOrUpdate(dbContract);*/
            /*Contract dbContract = new Contract();
            dbContract.setId(balanceDTO.getContractCode());
            dbContract.setCurrent(balanceDTO.getCurrent());
            dbContract.setConsolidate(balanceDTO.getConsolidate());
            contractService.saveOrUpdate(dbContract);*/
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
        if (curFlowDetail == FlowDetailEnum.JobErrorForeseensCancelUnKnow) {
            dbPrintJob.setFlow(FlowEnum.FlowIng.getCode());
        } else if (curFlowDetail == FlowDetailEnum.JobErrorForeseensCancel4xx) {
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
            //todo 碰到这种异常，保存进度，不保存transaction 和 frank 不修改金额
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

        //废弃，不批量保存了
        // 添加frank
        /*List<Frank> frankList = new ArrayList<>();
        for (FrankDTO frankDTO : transactionDTO.getFranks()) {
            Frank frank = new Frank();
            frank.setDmMessage(frankDTO.getDmMessage());
            frank.setId(AESUtils.createUUID());
            frank.setStatisticsId("");
            frank.setTransactionId(transaction.getId());
            frank.setCreatedTime(new Date());
            frankList.add(frank);
        }
        transactionMsgService.saveBatch(frankList);*/

        //如果一切OK
        if (curFlowDetail == FlowDetailEnum.JobEndSuccess) {
            /*Double dbCurrent = dbContract.getCurrent();
            Double dbConsolidate = dbContract.getConsolidate();

            //金额： 合同余额减去transaction的金额
            double newCurrent = DoubleKit.sub(dbCurrent, transaction.getAmount());

            //foreseen的金额
            Foreseen dbForeseen = foreseenService.getById(dbPrintJob.getForeseenId());
            Double usedConsolidate = dbForeseen.getTotalAmmount();

            double newConsolidate = DoubleKit.add(dbConsolidate, usedConsolidate);
            newConsolidate = DoubleKit.sub(newConsolidate, transaction.getCreditVal());

            dbContract.setCurrent(newCurrent);
            dbContract.setConsolidate(newConsolidate);
            contractService.saveOrUpdate(dbContract);*/

            /*dbContract.setId(balanceDTO.getContractCode());
            dbContract.setCurrent(balanceDTO.getCurrent());
            dbContract.setConsolidate(balanceDTO.getConsolidate());

            contractService.saveOrUpdate(dbContract);*/
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
}
