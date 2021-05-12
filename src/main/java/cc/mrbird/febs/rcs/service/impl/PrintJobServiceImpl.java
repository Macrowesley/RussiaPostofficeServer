package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.device.service.IDeviceService;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.kit.DoubleKit;
import cc.mrbird.febs.rcs.dto.manager.ForeseenDTO;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductDTO;
import cc.mrbird.febs.rcs.dto.manager.FrankDTO;
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

    private final PrintJobMapper printJobMapper;
    private final IForeseenService foreseenService;
    private final IForeseenProductService foreseenProductService;
    private final ITransactionService transactionService;
    private final IFrankService frankService;
    private final IDeviceService deviceService;
    private final IContractService contractService;

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
        wrapper.eq(PrintJob::getFlow,FlowEnum.FlowIng.getCode());
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
        return this.getOne(wrapper);
    }

    /**
     * job流程中，foreseens的进度更新
     *
     * @param dbDevice
     * @param flowDetailEnum
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeForeseensStatus(ForeseenDTO foreseenDTO,  FlowDetailEnum curFlowDetail) {
        /**
         创建job
         创建foreseens和ForeseenProduct
         */

        //创建job
        PrintJob printJob = new PrintJob();
        if (curFlowDetail == FlowDetailEnum.JobingForeseensSuccess){
            printJob.setFlow(FlowEnum.FlowIng.getCode());
        }else{
            printJob.setFlow(FlowEnum.FlowEnd.getCode());
        }

        printJob.setContractId(foreseenDTO.getContractId());
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
        BeanUtils.copyProperties(foreseenDTO,foreseen);
        foreseen.setForeseenStatus(FlowEnum.FlowEnd.getCode());
        foreseen.setUpdatedTime(new Date());
        foreseen.setCreatedTime(new Date());
        foreseenService.createForeseen(foreseen);

        List<ForeseenProduct> foreseenProductList = new ArrayList<>();
        for (ForeseenProductDTO productDto: foreseenDTO.getProducts()){
            ForeseenProduct foreseenProduct = new ForeseenProduct();
            foreseenProduct.setForeseenId(foreseen.getId());
            foreseenProduct.setCreatedTime(new Date());
            foreseenProductList.add(foreseenProduct);
        }

        foreseenProductService.saveBatch(foreseenProductList);
    }

    /**
     * job流程中，foreseensCancel的进度更新
     *
     * @param cancelJobFMDTO
     * @param flowDetailEnum
     */
    @Override
    public void changeForeseensCancelStatus(PrintJob dbPrintJob, CancelJobFMDTO cancelJobFMDTO, FlowDetailEnum curFlowDetail) {
        //更新PrintJob
        if (curFlowDetail == FlowDetailEnum.JobErrorForeseensCancelUnKnowError){
            dbPrintJob.setFlow(FlowEnum.FlowIng.getCode());
        }else{
            dbPrintJob.setFlow(FlowEnum.FlowEnd.getCode());
        }
        dbPrintJob.setUpdatedTime(new Date());
        dbPrintJob.setCancelMsgCode(cancelJobFMDTO.getCancelMsgCode());
        this.updatePrintJob(dbPrintJob);
    }

    /**
     * job流程中，Transaction的进度更新
     *
     * @param dbPrintJob
     * @param transactionDTO
     * @param curFlowDetail
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public Contract changeTransactionStatus(PrintJob dbPrintJob, Contract dbContract , TransactionDTO transactionDTO, FlowDetailEnum curFlowDetail) {
        //更新PrintJob
        if (curFlowDetail == FlowDetailEnum.JobErrorTransactionUnKnow){
            //todo 碰到这种异常，保存进度，不保存transaction 和 frank 不修改金额
            dbPrintJob.setFlow(FlowEnum.FlowIng.getCode());
            dbPrintJob.setUpdatedTime(new Date());
            this.updatePrintJob(dbPrintJob);
            return dbContract;
        }else{
            dbPrintJob.setFlow(FlowEnum.FlowEnd.getCode());
        }
        dbPrintJob.setUpdatedTime(new Date());
        this.updatePrintJob(dbPrintJob);

        //添加 transaction
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO,transaction);
        transaction.setTransactionStatus(FlowEnum.FlowEnd.getCode());
        transaction.setUpdatedTime(new Date());
        transaction.setCreatedTime(new Date());
        transactionService.createTransaction(transaction);

        //添加frank
        List<Frank> frankList = new ArrayList<>();
        for (FrankDTO frankDTO: transactionDTO.getFranks()) {
            Frank frank = new Frank();
            frank.setDmMessage(frankDTO.getDm_message());
            frank.setId(AESUtils.createUUID());
            frank.setStatisticsId("");
            frank.setTransactionId(transaction.getId());
            frank.setCreatedTime(new Date());
            frankList.add(frank);
        }
        frankService.saveBatch(frankList);

        //如果一切OK，更新contract的金额 todo 确定金额是哪个减哪个
        if (curFlowDetail == FlowDetailEnum.JobEndSuccess) {
            Double current = dbContract.getCurrent();
            Double consolidate = dbContract.getConsolidate();

            double newCurrent = DoubleKit.sub(current, transaction.getMailVal());
            double newConsolidate = DoubleKit.sub(consolidate, transaction.getCreditVal());

            dbContract.setCurrent(newCurrent);
            dbContract.setConsolidate(newConsolidate);
            contractService.saveOrUpdate(dbContract);
        }

        //返回最新的contract
        return dbContract;
    }

    @Override
    public Foreseen getForeseenById(String foreseenId) {
       return foreseenService.getById(foreseenId);
    }
}
