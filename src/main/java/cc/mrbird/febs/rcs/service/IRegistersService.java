package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Registers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【待定】 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:27
 */
public interface IRegistersService extends IService<Registers> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param registers registers
     * @return IPage<Registers>
     */
    IPage<Registers> findRegisterss(QueryRequest request, Registers registers);

    /**
     * 查询（所有）
     *
     * @param registers registers
     * @return List<Registers>
     */
    List<Registers> findRegisterss(Registers registers);

    /**
     * 新增
     *
     * @param registers registers
     */
    void createRegisters(Registers registers);

    /**
     * 修改
     *
     * @param registers registers
     */
    void updateRegisters(Registers registers);

    /**
     * 删除
     *
     * @param registers registers
     */
    void deleteRegisters(Registers registers);
}
