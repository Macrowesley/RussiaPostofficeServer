package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.dto.service.ChangeStatusRequestDTO;
import cc.mrbird.febs.rcs.entity.FrankMachine;
import cc.mrbird.febs.rcs.mapper.FrankMachineMapper;
import cc.mrbird.febs.rcs.service.IFrankMachineService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 机器信息 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:05
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FrankMachineServiceImpl extends ServiceImpl<FrankMachineMapper, FrankMachine> implements IFrankMachineService {

    private final FrankMachineMapper frankMachineMapper;

    @Override
    public IPage<FrankMachine> findFrankMachines(QueryRequest request, FrankMachine frankMachine) {
        LambdaQueryWrapper<FrankMachine> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<FrankMachine> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<FrankMachine> findFrankMachines(FrankMachine frankMachine) {
	    LambdaQueryWrapper<FrankMachine> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFrankMachine(FrankMachine frankMachine) {
        this.save(frankMachine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFrankMachine(FrankMachine frankMachine) {
        this.saveOrUpdate(frankMachine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFrankMachine(FrankMachine frankMachine) {
        LambdaQueryWrapper<FrankMachine> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    /**
     * 俄罗斯改变机器状态
     *
     * @param frankMachineId
     * @param changeStatusRequestDTO
     */
    @Override
    @Transactional(rollbackFor = RcsApiException.class)
    public void changeStatus(String frankMachineId, ChangeStatusRequestDTO changeStatusRequestDTO) throws RuntimeException{
        checkStatus(frankMachineId);
        FrankMachine frankMachine = new FrankMachine();
        frankMachine.setId(frankMachineId);
        frankMachine.setFutureFmStatus(changeStatusRequestDTO.getStatus().getType());
        frankMachine.setPostOffice(changeStatusRequestDTO.getPostOffice());
        //开始修改的话，把流程状态改成进行中
        frankMachine.setFlow(FlowEnum.FlowIng.getCode());
        this.saveOrUpdate(frankMachine);
    }

    private void checkStatus(String frankMachineId) {
        //todo 缓存要加上来
        LambdaQueryWrapper<FrankMachine> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FrankMachine::getId, frankMachineId);
        FrankMachine frankMachine = this.getOne(wrapper);

        if (frankMachine == null){
            throw new RcsApiException("无法找到id为" + frankMachineId + "的frankMachine");
        }

        if (frankMachine.getFlow() == FlowEnum.FlowIng.getCode()){
            throw new RcsApiException("上次修改没有完成，请稍等");
        }
    }
}
