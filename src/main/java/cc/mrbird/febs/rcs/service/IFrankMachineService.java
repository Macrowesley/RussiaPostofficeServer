package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.FrankMachine;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 机器信息 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:05
 */
public interface IFrankMachineService extends IService<FrankMachine> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param frankMachine frankMachine
     * @return IPage<FrankMachine>
     */
    IPage<FrankMachine> findFrankMachines(QueryRequest request, FrankMachine frankMachine);

    /**
     * 查询（所有）
     *
     * @param frankMachine frankMachine
     * @return List<FrankMachine>
     */
    List<FrankMachine> findFrankMachines(FrankMachine frankMachine);

    /**
     * 新增
     *
     * @param frankMachine frankMachine
     */
    void createFrankMachine(FrankMachine frankMachine);

    /**
     * 修改
     *
     * @param frankMachine frankMachine
     */
    void updateFrankMachine(FrankMachine frankMachine);

    /**
     * 删除
     *
     * @param frankMachine frankMachine
     */
    void deleteFrankMachine(FrankMachine frankMachine);
}
