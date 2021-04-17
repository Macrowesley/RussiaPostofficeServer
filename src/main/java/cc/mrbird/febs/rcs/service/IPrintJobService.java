package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
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
}
