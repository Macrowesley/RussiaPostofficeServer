package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionMsgFMDTO;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.common.kit.DoubleKit;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.dto.manager.FrankDTO;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.entity.Transaction;
import cc.mrbird.febs.rcs.entity.TransactionMsg;
import cc.mrbird.febs.rcs.mapper.TransactionMsgMapper;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
import cc.mrbird.febs.rcs.service.ITransactionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 交易表 Service实现
 *
 * @author mrbird
 * @date 2021-04-17 14:45:17
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TransactionMsgServiceImpl extends ServiceImpl<TransactionMsgMapper, TransactionMsg> implements ITransactionMsgService {

    @Autowired
    TransactionMsgMapper transactionMsgMapper;

    @Autowired
    ITransactionService transactionService;

    @Autowired
    IPrintJobService printJobService;

    @Override
    public IPage<TransactionMsg> findTransactionMsgs(QueryRequest request, TransactionMsg transactionMsg) {
        LambdaQueryWrapper<TransactionMsg> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<TransactionMsg> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<TransactionMsg> findTransactionMsgs(TransactionMsg transactionMsg) {
	    LambdaQueryWrapper<TransactionMsg> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTransactionMsg(TransactionMsg transactionMsg) {
        this.save(transactionMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransactionMsg(TransactionMsg transactionMsg) {
        this.saveOrUpdate(transactionMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransactionMsg(TransactionMsg transactionMsg) {
        LambdaQueryWrapper<TransactionMsg> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}

    @Override
    public boolean checkIsSameWithLastOne(TransactionMsgFMDTO transactionMsgFMDTO) {
        /*LambdaQueryWrapper<TransactionMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TransactionMsg::getAmount, MoneyUtils.changeF2Y(transactionMsgFMDTO.getTotalAmount()));
        wrapper.eq(TransactionMsg::getCount, transactionMsgFMDTO.getTotalCount());
        wrapper.eq(TransactionMsg::getDmMsg, transactionMsgFMDTO.getDmMsg());
        wrapper.eq(TransactionMsg::getStatus, transactionMsgFMDTO.getStatus());
//        wrapper.eq(TransactionMsg::getFrankMachineId, transactionMsgFMDTO.getFrankMachineId());
        return this.baseMapper.selectCount(wrapper) > 0;*/
        TransactionMsg lastestMsg = getLastestMsg(transactionMsgFMDTO.getId());
        if (lastestMsg!=null && lastestMsg.getStatus().equals(transactionMsgFMDTO.getStatus())
                && String.valueOf(lastestMsg.getAmount()).equals(transactionMsgFMDTO.getTotalAmount())
                && lastestMsg.getCount() == Long.valueOf(transactionMsgFMDTO.getTotalCount())
                && lastestMsg.getFrankMachineId().equals(transactionMsgFMDTO.getFrankMachineId())) {
            return true;
        }
        return false;
    }

    /**
     * 根据transactionId得到打印任务中所有的dmMsg列表,按时间顺序
     *
     * @param transactionId
     * @return
     */
    @Override
    public List<TransactionMsg> selectByTransactionId(String transactionId) {
        LambdaQueryWrapper<TransactionMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TransactionMsg::getTransactionId, transactionId);
        wrapper.orderByAsc(TransactionMsg::getCreatedTime);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public TransactionMsg getLastestMsg(String transactionId) {
        LambdaQueryWrapper<TransactionMsg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(TransactionMsg::getCreatedTime).last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public DmMsgDetail getDmMsgDetail(List<TransactionMsg> msgList) {
        if (msgList.size() % 2 != 0) {
            throw new FmException(FMResultEnum.DmmsgIsNotFinish.getCode(),"批次记录为奇数，有没有完成的批次");
        }
        DmMsgDetail dmMsgDetail = new DmMsgDetail();
        //当前任务已经打印的总数量
        int actualCount = 0;
        //当前任务已经打印的总金额
        double actualAmount = 0.0;

        int arraySize = msgList.size() / 2;

        //list是根据时间顺序序的，互相相减即可
        //msg是批次的开始记录
        TransactionMsg beginMsg = null;
        //的msg是批次的结束记录
        TransactionMsg endMsg = null;
        List<FrankDTO> frankDTOList = new ArrayList<>();
        for (int i = 0; i < msgList.size(); i++) {
            if (i % 2 == 0) {
                beginMsg = msgList.get(i);
            }else{
                endMsg = msgList.get(i);
                //list是顺序的，array数组也是正序的

                if (beginMsg != null && endMsg != null && endMsg.getCount() != beginMsg.getCount()) {
                    //log.info("\nbeginMsg={},\nendMsg={}",beginMsg,endMsg);
                    actualCount += endMsg.getCount() - beginMsg.getCount();
                    actualAmount += DoubleKit.sub(endMsg.getAmount(),beginMsg.getAmount());
                    frankDTOList.add(new FrankDTO(endMsg.getDmMsg()));
                }
            }
        }
        FrankDTO[] frankDTOArray = frankDTOList.toArray(new FrankDTO[frankDTOList.size()]);
        dmMsgDetail.setActualCount(actualCount);
        dmMsgDetail.setActualAmount(String.valueOf(MoneyUtils.changeY2F(actualAmount)));
        dmMsgDetail.setFranks(frankDTOArray);
        return dmMsgDetail;
    }

    /**
     * 机器开机的时候获取dmMsgDetail
     * 判断上一个批次是否结束
     *    结束了，同时返回进度给机器
     *    未结束：找到没有结束的那个批次，得到dm_msg等信息，保存这个dm_msg到数据库中，同时返回进度给机器
     *
     * @param transactionId
     */
    @Override
    @Transactional(rollbackFor = FmException.class)
    public DmMsgDetail getDmMsgDetailOnFmStart(String transactionId, TransactionMsgFMDTO transactionMsgFMDTO) {
        List<TransactionMsg> transactionMsgList = selectByTransactionId(transactionId);
        if (transactionMsgList.size() % 2 != 0){
            double fmTotalAmount = MoneyUtils.changeF2Y(transactionMsgFMDTO.getTotalAmount());
            long fmTotalCount = Long.valueOf(transactionMsgFMDTO.getTotalCount());

            TransactionMsg lastestMsg = getLastestMsg(transactionMsgFMDTO.getId());
            if (lastestMsg.getCount() > fmTotalCount || DoubleKit.isV1BiggerThanV2(lastestMsg.getAmount(),fmTotalAmount)){
                throw new FmException(FMResultEnum.CountOrAmountSmallThenDb.getCode(),"transactionMsg信息中的的总数量或者总金额小于数据库的值");
            }

            //如果不是偶数，说明有一个批次没有结束，把当前的数据作为批次的结束
            //找到没有结束的那个批次，得到dm_msg等信息，保存这个dm_msg到数据库中
            TransactionMsg transactionMsg = new TransactionMsg();
            transactionMsg.setTransactionId(transactionId);
            transactionMsg.setCount(fmTotalCount);
            transactionMsg.setAmount(fmTotalAmount);
            transactionMsg.setDmMsg(transactionMsgList.get(transactionMsgList.size()-1).getDmMsg());
            transactionMsg.setFrankMachineId(transactionMsgFMDTO.getFrankMachineId());
            transactionMsg.setStatus("2");
            transactionMsg.setCreatedTime(new Date());
            this.createTransactionMsg(transactionMsg);
            transactionMsgList.add(transactionMsg);
        }

        //到了这里，list是偶数的，倒叙相减，累计总金额和数量
        return this.getDmMsgDetail(transactionMsgList);
    }

    /**
     * 保存批次发送的内容,返回transactionId
     *
     * @param transactionMsgFMDTO
     */
    @Override
    @Transactional(rollbackFor = FmException.class)
    public String saveMsg(TransactionMsgFMDTO transactionMsgFMDTO) {
        //创建transaction
        //dbPrintJob.setTransactionId(transactionDTO.getId());
        int idType = transactionMsgFMDTO.getIdType();
        double fmTotalAmount = MoneyUtils.changeF2Y(transactionMsgFMDTO.getTotalAmount());
        Long fmTotalCount = Long.valueOf(transactionMsgFMDTO.getTotalCount());

        //金额数量是累加的，不能小于数据库的值
        TransactionMsg lastestMsg = getLastestMsg(transactionMsgFMDTO.getId());
        if (lastestMsg.getCount() > fmTotalCount || DoubleKit.isV1BiggerThanV2(lastestMsg.getAmount(),fmTotalAmount)){
            throw new FmException(FMResultEnum.CountOrAmountSmallThenDb.getCode(),"transactionMsg信息中的的总数量或者总金额小于数据库的值");
        }

        //判断是否有和上一个msg一样
        if (lastestMsg!=null && lastestMsg.getStatus().equals(transactionMsgFMDTO.getStatus())
                && String.valueOf(lastestMsg.getAmount()).equals(transactionMsgFMDTO.getTotalAmount())
                && lastestMsg.getCount() == fmTotalCount
                && lastestMsg.getFrankMachineId().equals(transactionMsgFMDTO.getFrankMachineId())) {
            throw new FmException(FMResultEnum.TransactionMsgExist.getCode(),"transactionMsg已经存在，不能新建");
        }

        //如果最新的消息不为空，且status一样，那么，肯定是上一个批次没有结束
        String status = idType == 1 ? "1" : transactionMsgFMDTO.getStatus();
        //判断批次是否完成
        if (lastestMsg != null && lastestMsg.getStatus().equals(status)){
            throw new FmException(FMResultEnum.DmmsgIsNotFinish.getCode(),"有没有完成的批次,那个批次信息为：" + lastestMsg.toString());
        }

        String transactionId = transactionMsgFMDTO.getId();
        if (idType == 1){
            log.info("foreseen之后第一个批次信息处理");
            //id是ForeseensId
            /**
             * 1. 创建transaction
             * 2. dbPrintJob保存transactionId
             * 3. 保存transactionMsgFMDTO
             */
            String foreseenId = transactionMsgFMDTO.getId();
            PrintJob dbPrintJob = printJobService.getByForeseenId(foreseenId);
            //如果已经有了transactionId,就不能创建了
            if (!StringUtils.isEmpty(dbPrintJob.getTransactionId())){
                throw new FmException(FMResultEnum.TransactionExist.getCode(),"transaction已经存在，不能新建");
            }

            transactionId = AESUtils.createUUID();
            Transaction transaction = new Transaction();
            transaction.setId(transactionId);
            transaction.setForeseenId(foreseenId);
            transaction.setStartDateTime(DateKit.createRussiatime(new Date()));
            transaction.setStopDateTime("");
            transaction.setUserId("");
            transaction.setCreditVal(0.0D);
            transaction.setAmount(0.0D);
            transaction.setCount(0);
            transaction.setGraphId("");
            transaction.setTaxVersion("");
            transaction.setTransactionStatus(FlowEnum.FlowIng.getCode());
            transaction.setCreatedTime(new Date());
            transactionService.createTransaction(transaction);


            dbPrintJob.setTransactionId(transactionId);
            printJobService.updatePrintJob(dbPrintJob);
        }
        /**
         * id是TransactionId
         * 保存transactionMsgFMDTO
         */

        TransactionMsg transactionMsg = new TransactionMsg();
        transactionMsg.setTransactionId(transactionId);
        transactionMsg.setCount(fmTotalCount);
        transactionMsg.setAmount(fmTotalAmount);
        transactionMsg.setDmMsg(transactionMsgFMDTO.getDmMsg());
        transactionMsg.setFrankMachineId(transactionMsgFMDTO.getFrankMachineId());
        transactionMsg.setStatus(status);
        transactionMsg.setCreatedTime(new Date());
        this.createTransactionMsg(transactionMsg);

        return transactionId;
    }

    /**
     * 完成任务后，得到dmMsgDetail，来构建给俄罗斯的内容
     * transaction结束后，根据transactionid得到所有的dm_msg信息，
     * 然后按时间顺序，一个批次前后记录相减，得到一个批次实际打印金额和重量，累计得到总金额和总件数，并得到dm_msg数组，拼接数据，发送给俄罗斯
     *
     * @param transactionId
     * @return
     */
    @Override
    public DmMsgDetail getDmMsgDetailAfterFinishJob(String transactionId) {
        List<TransactionMsg> transactionMsgList = selectByTransactionId(transactionId);
        return this.getDmMsgDetail(transactionMsgList);
    }
}
