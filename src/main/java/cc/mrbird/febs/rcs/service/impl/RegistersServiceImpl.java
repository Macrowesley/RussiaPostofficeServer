package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.entity.Registers;
import cc.mrbird.febs.rcs.mapper.RegistersMapper;
import cc.mrbird.febs.rcs.service.IRegistersService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 【待定】 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:27
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RegistersServiceImpl extends ServiceImpl<RegistersMapper, Registers> implements IRegistersService {

    @Autowired
    RegistersMapper registersMapper;

    @Override
    public IPage<Registers> findRegisterss(QueryRequest request, Registers registers) {
        LambdaQueryWrapper<Registers> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Registers> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Registers> findRegisterss(Registers registers) {
	    LambdaQueryWrapper<Registers> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRegisters(Registers registers) {
        this.save(registers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRegisters(Registers registers) {
        this.saveOrUpdate(registers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRegisters(Registers registers) {
        LambdaQueryWrapper<Registers> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
