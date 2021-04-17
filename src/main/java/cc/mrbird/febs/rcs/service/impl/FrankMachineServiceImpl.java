package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
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
}
