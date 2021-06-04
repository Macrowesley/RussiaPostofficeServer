package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.device.entity.Device;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.dto.manager.ForeseenDTO;
import cc.mrbird.febs.rcs.dto.manager.TransactionDTO;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.entity.PrintJob;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 打印任务表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:44:46
 */
public interface IPrintJobService extends IService<PrintJob> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param printJob printJob
     * @return IPage<PrintJob>
     */
    IPage<PrintJob> findPrintJobs(QueryRequest request, PrintJob printJob);

    /**
     * 查询（所有）
     *
     * @param printJob printJob
     * @return List<PrintJob>
     */
    List<PrintJob> findPrintJobs(PrintJob printJob);

    /**
     * 新增
     *
     * @param printJob printJob
     */
    void createPrintJob(PrintJob printJob);

    /**
     * 修改
     *
     * @param printJob printJob
     */
    void updatePrintJob(PrintJob printJob);

    /**
     * 删除
     *
     * @param printJob printJob
     */
    void deletePrintJob(PrintJob printJob);

    /**
     * 通过frankMachineId找到没有闭环的打印任务
     * @param frankMachineId
     * @return
     */
    PrintJob getUnFinishJobByFmId(String frankMachineId);

    PrintJob getLastestJobByFmId(String frankMachineId);

    PrintJob getByForeseenId(String foreseenId);

    /**
     * job流程中，foreseens的进度更新
     */
    void changeForeseensStatus(ForeseenDTO foreseenDTO,  FlowDetailEnum curFlowDetail);

    /**
     * job流程中，foreseensCancel的进度更新
     * @param cancelJobFMDTO
     * @param flowDetailEnum
     */
    void changeForeseensCancelStatus(PrintJob dbPrintJob, CancelJobFMDTO cancelJobFMDTO, FlowDetailEnum curFlowDetail);

    /**
     * job流程中，Transaction的进度更新
     * @param dbPrintJob
     * @param transactionDTO
     * @param curFlowDetail
     */
    Contract changeTransactionStatus(PrintJob dbPrintJob,Contract dbContract, TransactionDTO transactionDTO, FlowDetailEnum curFlowDetail);


    Foreseen getForeseenById(String foreseenId);


    boolean checkPrintJobFinish(String frankMachineId);
}
