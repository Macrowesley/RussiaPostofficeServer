package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFMDTO;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.dto.machine.PrintProgressInfo;
import cc.mrbird.febs.rcs.dto.manager.ManagerBalanceDTO;
import cc.mrbird.febs.rcs.dto.manager.TransactionDTO;
import cc.mrbird.febs.rcs.dto.service.PrintJobDTO;
import cc.mrbird.febs.rcs.dto.ui.PrintJobAddDto;
import cc.mrbird.febs.rcs.dto.ui.PrintJobUpdateDto;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.Foreseen;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.entity.Transaction;
import cc.mrbird.febs.rcs.vo.PrintJobExcelVO;
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
    IPage<PrintJob> findPrintJobs(QueryRequest request, PrintJobDTO printJobDto);

    /**
     * 查询（所有）
     *
     * @param printJob printJob
     * @return List<PrintJob>
     */
    List<PrintJob> findPrintJobs(PrintJobDTO printJobDto);

    /**
     * 返回excel需要的数据
     * @param printJob
     * @return
     */
    List<PrintJobExcelVO> selectExcelData(PrintJobDTO printJobDto);

    /**
     * 新增
     *
     * @param printJob printJob
     */
    void createPrintJob(PrintJob printJob);

    /**
     * 创建前端打印订单
     * @param printJobAddDto
     */
    void createPrintJobDto(PrintJobAddDto printJobAddDto);

    /**
     * 编辑
     * @param printJobUpdateDto
     */
    void editPrintJob(PrintJobAddDto printJobUpdateDto);

    /**
     * 修改
     * @param printJob
     */
    void updatePrintJob(PrintJob printJob);

    void editAndUpdatePrintJob(PrintJob printJob);

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

    PrintJob getLastestJobByFmId(String frankMachineId, int type);

    PrintJob getByPrintJobId(int printJobId);

    PrintJob getByForeseenId(String foreseenId);

    PrintJob getByTransactionId(String transactionId);

    /**
     * job流程中，foreseens的进度更新
     */
    PrintJob changeForeseensStatus(ForeseenFMDTO foreseenFmDto, PrintJob dbPrintJob, FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO);

    /**
     * job流程中，foreseensCancel的进度更新
     * @param cancelJobFMDTO
     * @param flowDetailEnum
     */
    void changeForeseensCancelStatus(PrintJob dbPrintJob, CancelJobFMDTO cancelJobFMDTO, FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO);

    /**
     * job流程中，Transaction的进度更新
     * @param dbPrintJob
     * @param transactionDTO
     * @param curFlowDetail
     */
    Contract changeTransactionStatus(PrintJob dbPrintJob,Contract dbContract, TransactionDTO transactionDTO,  FlowDetailEnum curFlowDetail, ManagerBalanceDTO balanceDTO);


    Foreseen getForeseenById(String foreseenId);

    Transaction getTransactionById(String transactionId);


    boolean checkPrintJobFinish(String frankMachineId);


    /**
     * 处理打印，可能有多种情况
     * @param printJobId
     */
    void doPrintJob(int printJobId);

    /**
     * 开始取消打印任务
     * @param id
     */
    void cancelPrintJob(int id);

    /**
     * 获取PC上的订单打印进度
     * @param dbPrintJob
     * @return
     */
    PrintProgressInfo getProductPrintProgress(PrintJob dbPrintJob);


}
