package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionMsgFMDTO;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.rcs.api.CheckUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.dto.manager.FrankDTO;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.entity.Transaction;
import cc.mrbird.febs.rcs.entity.TransactionMsg;
import cc.mrbird.febs.rcs.entity.TransactionMsgBatchInfo;
import cc.mrbird.febs.rcs.mapper.TransactionMsgMapper;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
import cc.mrbird.febs.rcs.service.ITransactionService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.bson.Document;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ????????? Service??????
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

    @Autowired
    RedisService redisService;

    @Autowired
    CheckUtils checkUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * ????????????mongodb????????????
     */
    boolean useMongodb = true;

    @Override
    public IPage<TransactionMsg> findTransactionMsgs(QueryRequest request, TransactionMsg transactionMsg) {
        //????????????????????????mongodb?
        if(useMongodb){
            Pageable pageable = PageRequest.of(request.getPageNum() - 1,request.getPageSize());
            Query query = new Query(Criteria.where("transaction_id").is(transactionMsg.getTransactionId()));

            //???????????????
            long total = mongoTemplate.count(query, TransactionMsg.class);

            List<TransactionMsg> list = this.mongoTemplate.find(query.with(pageable).with(Sort.by(Sort.Order.asc("_id"))), TransactionMsg.class);

            Page<TransactionMsg> page = new Page<>(request.getPageNum(),request.getPageSize());

            page.setTotal(total);
            page.setRecords(list);

            return page;
        }else{
            LambdaQueryWrapper<TransactionMsg> queryWrapper = new LambdaQueryWrapper<>();
            // TODO ??????????????????
            Page<TransactionMsg> page = new Page<>(request.getPageNum(), request.getPageSize());
            return this.page(page, queryWrapper);
        }

    }

    @Override
    public List<TransactionMsg> findTransactionMsgs(String transactionId) {
        if(useMongodb){
            return this.mongoTemplate.find(new Query(Criteria.where("transaction_id").is(transactionId)).with(Sort.by(Sort.Order.asc("_id"))),TransactionMsg.class);
        }
	    LambdaQueryWrapper<TransactionMsg> queryWrapper = new LambdaQueryWrapper<>();
		// TODO ??????????????????
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTransactionMsg(TransactionMsg transactionMsg) {
        if(useMongodb){
//            transactionMsg.setCreatedTime(new Date());
//            transactionMsg.setStatus("1");
//            transactionMsg.setCode("2102");
//            transactionMsg.setDmMsg("01PM64313100022032210020000000605000500020110001000001770020");
//            transactionMsg.setAmount(100L);
//            transactionMsg.setCount(500L);
//            transactionMsg.setFrankMachineId("PM100200");
//            transactionMsg.setTransactionId("58702413-b657-484b-81c0-c7899dc2c5b7");


            transactionMsg.setId(redisService.getIncr("msgId"));
            this.saveMsgToMongodb(transactionMsg);
        }else{
            this.save(transactionMsg);
        }
    }

    private void saveMsgToMongodb(TransactionMsg transactionMsg) {
        this.mongoTemplate.insert(transactionMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransactionMsg(TransactionMsg transactionMsg) {
        if(useMongodb){
            //?????????????????????????????????????????????????????????
            /*Query query = Query.query(Criteria.where("_id").is(transactionMsg.getId()));
            Update update = new Update();
            update.set("_id", transactionMsg.getId());
            mongoTemplate.upsert(query,update,TransactionMsg.class,"transactionMsg");*/
        }else{
            this.saveOrUpdate(transactionMsg);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransactionMsg(TransactionMsg transactionMsg) {
        if(useMongodb){
            Query query = Query.query(Criteria.where("_id").is(transactionMsg.getId()));
            mongoTemplate.remove(query, TransactionMsg.class);

        }else{
            LambdaQueryWrapper<TransactionMsg> wrapper = new LambdaQueryWrapper<>();
            // TODO ??????????????????
            this.remove(wrapper);
        }
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
                && lastestMsg.getCount().equals(Long.valueOf(transactionMsgFMDTO.getTotalCount()))
                && lastestMsg.getFrankMachineId().equals(transactionMsgFMDTO.getFrankMachineId())) {
            return true;
        }
        return false;
    }

    /**
     * ??????transactionId??????????????????????????????dmMsg??????,???????????????
     *
     * @param transactionId
     * @return
     */
    @Override
    public List<TransactionMsg> selectByTransactionId(String transactionId) {
        if (useMongodb) {
            return selectByTransactionIdInMongdb(transactionId);
        } else {
            LambdaQueryWrapper<TransactionMsg> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TransactionMsg::getTransactionId, transactionId);
            wrapper.orderByAsc(TransactionMsg::getId);
            return this.baseMapper.selectList(wrapper);
        }
    }

    /**
     * ???????????????????????????
     * @param transactionId
     * @return
     */
    @Override
    public TransactionMsg getLastestMsg(String transactionId) {
        TransactionMsg transactionMsg = null;
        if(useMongodb){
            List<TransactionMsg> transactionMsgList = selectByTransactionIdInMongdb(transactionId);
            if(transactionMsgList.size()>0){
                transactionMsg =  transactionMsgList.get(transactionMsgList.size()-1);
            }
        }else{
            LambdaQueryWrapper<TransactionMsg> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TransactionMsg::getTransactionId, transactionId);
            queryWrapper.orderByDesc(TransactionMsg::getId).last("limit 1");
            transactionMsg = this.getOne(queryWrapper);
        }
        return transactionMsg;

    }

    private List<TransactionMsg> selectByTransactionIdInMongdb(String transactionId) {
        return mongoTemplate.find(new Query(Criteria.where("transaction_id").is(transactionId)).with(Sort.by(Sort.Order.asc("_id"))),TransactionMsg.class);
    }

    /**
     *
     * @param msgList
     * @param needToRussiaList ????????????????????????transaction??????????????????list
     * @param needRealPrintCount ???????????????????????????????????????????????????
     *                           PS:
     *                           actualCount?????????job?????????????????????,???????????????????????????????????????????????????,???????????????????????????
     *
     * @return
     */
    @Override
    public DmMsgDetail getDmMsgDetail(List<TransactionMsg> msgList, boolean needToRussiaList, boolean needRealPrintCount) {
        if (msgList.size() % 2 != 0) {
            throw new FmException(FMResultEnum.DmmsgIsNotFinish.getCode(),"????????????????????????????????????????????????");
        }
        DmMsgDetail dmMsgDetail = new DmMsgDetail();
        //????????????????????????????????????????????????????????????
        long batchCount = 0;
        //????????????????????????????????????
        int actualCount = 0;
        //????????????????????????????????????
        int actualAmount = 0;

        int firstPos = 29;
        int endPos = 37;
        String tempDmMsg = "";
        String firstStr = "";
        String endStr = "";

        int arraySize = msgList.size() / 2;

        //list????????????????????????????????????????????????
        //msg????????????????????????
        TransactionMsg beginMsg = null;
        //???msg????????????????????????
        TransactionMsg endMsg = null;
        List<FrankDTO> frankDTOList = new ArrayList<>();
        //??????????????????
        HashMap<String, Integer> productRealPrintCountMap = new HashMap<>();
        for (int i = 0; i < msgList.size(); i++) {
            if (i % 2 == 0) {
                beginMsg = msgList.get(i);
                log.info("-------------------------");
//                log.info("beginMsg = " + beginMsg.toString());
            }else{
                endMsg = msgList.get(i);
//                log.info("endMsg = " + endMsg.toString());
                //list???????????????array?????????????????????
                if (beginMsg != null && endMsg != null && !(endMsg.getCount().equals(beginMsg.getCount()))) {
                    //log.info("\nbeginMsg={},\nendMsg={}",beginMsg,endMsg);
                    //??????????????????????????????????????????????????????????????????
                    batchCount = endMsg.getCount() - beginMsg.getCount();
                    actualCount += batchCount;
//                    log.info("i={}, code = {}, endMsg.getCount()={},  beginMsg.getAmount()={}, batchCount={},actualCount = {}", i,  beginMsg.getCode(), endMsg.getCount(), beginMsg.getCount(), batchCount, actualCount);

                    long tempAmount = endMsg.getAmount() - beginMsg.getAmount();
                    //???????????????????????????????????????????????????????????????????????????
                    actualAmount += tempAmount >= 0 ? tempAmount : (4294967295L - beginMsg.getAmount() + endMsg.getAmount() + 1);
                    //log.info("i={}, code = {}, endMsg.getAmount()={},  beginMsg.getAmount()={}, tempAmount={},actualAmount = {}", i, beginMsg.getCode(), endMsg.getAmount(), beginMsg.getAmount(), tempAmount, actualAmount);
                    //???????????????dmMsg??????????????????????????????????????????????????????actualCount??????
                    //!45!01NE6431310001410210000000000050000024002100000100001010
                    if (needToRussiaList) {
                        tempDmMsg = beginMsg.getDmMsg();
                        firstStr = tempDmMsg.substring(0, firstPos);
                        int pieceCount = Integer.valueOf(tempDmMsg.substring(firstPos, endPos));
                        endStr = tempDmMsg.substring(endPos);
                        for (int j = 0; j < batchCount; j++) {
                            frankDTOList.add(new FrankDTO(firstStr + String.format("%08d", (pieceCount + j)) + endStr));
                        }
                    }

                    //??????????????????????????????????????????
                    if (needRealPrintCount){
                        if (productRealPrintCountMap.containsKey(beginMsg.getCode())){
//                            log.info("??????" + beginMsg.getCode() + " getCode = " + productRealPrintCountMap.get(beginMsg.getCode()));
                            productRealPrintCountMap.put(beginMsg.getCode(), (int) (batchCount + productRealPrintCountMap.get(beginMsg.getCode())));
                        }else{
                            productRealPrintCountMap.put(beginMsg.getCode(), (int) batchCount);
//                            log.info("?????????" + beginMsg.getCode() + " getCode = " + productRealPrintCountMap.get(beginMsg.getCode()));
                        }
                    }
                }
            }
        }
        dmMsgDetail.setActualCount(actualCount);
        dmMsgDetail.setActualAmount(String.valueOf(actualAmount));
//        log.info("String.valueOf(actualAmount) = " + String.valueOf(actualAmount));
        if (needToRussiaList) {
            FrankDTO[] frankDtoArray = frankDTOList.toArray(new FrankDTO[frankDTOList.size()]);
            dmMsgDetail.setFranks(frankDtoArray);
        }

        if (needRealPrintCount){
            dmMsgDetail.setProductCountMap(productRealPrintCountMap);
        }

        return dmMsgDetail;
    }


    /**
     * ??????transactionId???????????????msg?????????????????????msg????????????????????????
     *
     * @param transactionId
     * @return
     */
    @Override
    public List<TransactionMsgBatchInfo> mergeMsgList(String transactionId) {
        //1. ???????????????transaction????????????msg
        List<TransactionMsg> transactionMsgs = selectByTransactionId(transactionId);
        List<TransactionMsgBatchInfo> transactionMsgBatchInfoList= new ArrayList<>();

        /**
         * 1. ?????????msg?????????code??? singleWeight???batchCount ??????????????????
         * 2. ???????????????????????????????????????????????????????????????
         * 3. ?????????????????????list
         */
        Map<String, List<TransactionMsg>> collect = transactionMsgs.stream().collect(
                Collectors.groupingBy(bean ->
                        bean.getCode() + "_" + bean.getSingleWeight() + "_" + bean.getBatchCount()
                ));

        collect.forEach((key, msgList) -> {
            int size = msgList.size();
//            log.info("key={},  value.size={}" ,key, size);

            DmMsgDetail dmMsgDetail = getDmMsgDetail(msgList, false, false);
//            log.info("dmMsgDetail = " + dmMsgDetail.toString());

            TransactionMsg firstMsg = msgList.get(0);
            TransactionMsg endMsg = msgList.get(size - 1);
            Integer foreseenOneBatchCount = firstMsg.getBatchCount();
            Integer singleWeight = firstMsg.getSingleWeight();
            //?????????
            double fixedValue = firstMsg.getFixedValue().doubleValue()/100;
            int transactionOneBatchCount = dmMsgDetail.getActualCount();


            TransactionMsgBatchInfo resultBean = new TransactionMsgBatchInfo();
            resultBean.setStartDate(firstMsg.getCreatedTime());
            resultBean.setEndDate(endMsg.getCreatedTime());
            resultBean.setTaxRegionType(firstMsg.getRegionType());
            resultBean.setTaxLabelRu(firstMsg.getLabelRu());
            resultBean.setForeseenOneBatchCount(foreseenOneBatchCount);
            resultBean.setForeseenOneBatchWeight(foreseenOneBatchCount * singleWeight);
            resultBean.setTaxFixedValue(fixedValue);
            resultBean.setCallculationAmount((double) (fixedValue * foreseenOneBatchCount));
            resultBean.setTransactionOneBatchCount(transactionOneBatchCount);
            resultBean.setTransactionOneBatchWeight(transactionOneBatchCount * singleWeight);
            resultBean.setTaxRealSaleRate(fixedValue);
            resultBean.setStartMsgId(firstMsg.getId());
            resultBean.setCode(firstMsg.getCode());

            transactionMsgBatchInfoList.add(resultBean);

            /*log.info("???????????????list??????, size = " + size);
            msgList.forEach(bean -> {
                log.info(bean.toString());
            });
            log.info("??????????????????????????????" + resultBean.toString());*/
        });

        List<TransactionMsgBatchInfo> resultList = transactionMsgBatchInfoList
                .stream()
                .sorted(Comparator.comparing(TransactionMsgBatchInfo::getStartMsgId))
                .collect(Collectors.toList());
        return resultList;
    }

    /**
     * ???????????????????????????dmMsgDetail
     * ?????????????????????????????????
     *    ???????????????????????????????????????
     *    ??????????????????????????????????????????????????????dm_msg????????????????????????dm_msg?????????????????????????????????????????????
     *
     * @param transactionId
     */
    @Override
    @Transactional(rollbackFor = FmException.class)
    public DmMsgDetail getDmMsgDetailOnFmStart(String transactionId, TransactionMsgFMDTO transactionMsgFMDTO) {
        List<TransactionMsg> transactionMsgList = selectByTransactionId(transactionId);
        if (transactionMsgList.size() % 2 != 0){
            TransactionMsg lastestMsg = transactionMsgList.get(transactionMsgList.size() - 1);
            long fmTotalAmount = Long.valueOf(transactionMsgFMDTO.getTotalAmount());
            long fmTotalCount = Long.valueOf(transactionMsgFMDTO.getTotalCount());

//            TransactionMsg lastestMsg = getLastestMsg(transactionId);
            if (lastestMsg!=null) {
                if (lastestMsg.getCount() > fmTotalCount) {
                    throw new FmException(FMResultEnum.CountOrAmountSmallThenDb.getCode(), "transactionMsg????????????????????????????????????????????????????????????");
                }
            }

            //????????????????????????????????????????????????????????????????????????????????????????????????
            //??????????????????????????????????????????dm_msg????????????????????????dm_msg???????????????
            TransactionMsg transactionMsg = new TransactionMsg();
            transactionMsg.setTransactionId(transactionId);
            transactionMsg.setCode(lastestMsg.getCode());
            transactionMsg.setCount(fmTotalCount);
            transactionMsg.setAmount(fmTotalAmount);
            transactionMsg.setBatchCount(lastestMsg.getBatchCount());
            transactionMsg.setFixedValue(Integer.valueOf(lastestMsg.getFixedValue()));
            transactionMsg.setSingleWeight(Integer.valueOf(lastestMsg.getSingleWeight()));
            transactionMsg.setRegionType(lastestMsg.getRegionType());
            transactionMsg.setLabelRu(lastestMsg.getLabelRu());
            transactionMsg.setDmMsg(lastestMsg.getDmMsg());
            transactionMsg.setFrankMachineId(transactionMsgFMDTO.getFrankMachineId());
            transactionMsg.setStatus("2");
            transactionMsg.setCreatedTime(new Date());
            this.createTransactionMsg(transactionMsg);
            transactionMsgList.add(transactionMsg);
        }

        //???????????????list??????????????????????????????????????????????????????
        return this.getDmMsgDetail(transactionMsgList, false, false);
    }

    /**
     * ???????????????????????????,??????transactionId
     *
     * @param transactionMsgFMDTO
     */
    @Override
    @Transactional(rollbackFor = FmException.class)
    public String saveMsg(TransactionMsgFMDTO transactionMsgFMDTO) {
        //??????transaction
        //dbPrintJob.setTransactionId(transactionDTO.getId());
        int idType = transactionMsgFMDTO.getIdType();
        long fmTotalAmount = Long.valueOf(transactionMsgFMDTO.getTotalAmount());
        Long fmTotalCount = Long.valueOf(transactionMsgFMDTO.getTotalCount());

        //????????????????????????????????????status??????????????????????????????????????????????????????
        String status = idType == 1 ? "1" : transactionMsgFMDTO.getStatus();

        //??????????????????????????????????????????????????????
        TransactionMsg lastestMsg = getLastestMsg(transactionMsgFMDTO.getId());

        if (lastestMsg != null) {
            log.info("lastestMsg={}",lastestMsg.toString());
            if (lastestMsg.getCount() > fmTotalCount){
                throw new FmException(FMResultEnum.CountOrAmountSmallThenDb.getCode(), "transactionMsg????????????????????????????????????????????????????????????");
            }

            //???????????????????????????msg??????
            /*if (lastestMsg.getStatus().equals(transactionMsgFMDTO.getStatus())
                    && String.valueOf(lastestMsg.getAmount()).equals(transactionMsgFMDTO.getTotalAmount())
                    && lastestMsg.getCount().equals(fmTotalCount)
                    && lastestMsg.getFrankMachineId().equals(transactionMsgFMDTO.getFrankMachineId())) {
                throw new FmException(FMResultEnum.TransactionMsgExist.getCode(),"transactionMsg???????????????????????????");
            }*/

            //????????????????????????
            if (lastestMsg.getStatus().equals(status)){
                throw new FmException(FMResultEnum.DmmsgIsNotFinish.getCode(),"????????????????????????,????????????????????????" + lastestMsg.toString());
            }
        }

        String transactionId = null;
        PrintJob dbPrintJob = null;
        if (idType == 1){
            log.info("foreseen?????????????????????????????????");
            //id???ForeseensId
            /**
             * 1. ??????transaction
             * 2. dbPrintJob??????transactionId
             * 3. ??????transactionMsgFMDTO
             */
            String foreseenId = transactionMsgFMDTO.getId();
            dbPrintJob = printJobService.getByForeseenId(foreseenId);
            //??????????????????transactionId,??????????????????
            if (!StringUtils.isEmpty(dbPrintJob.getTransactionId())){
                throw new FmException(FMResultEnum.TransactionExist.getCode(),"transaction???????????????????????????");
            }

            //????????????????????????????????????
            checkUtils.checkTransactionFlowDetailIsOk(foreseenId,transactionMsgFMDTO.getFrankMachineId());

            if (!FebsConstant.IS_TEST_NETTY) {
                transactionId = AESUtils.createUUID();
            }else{
                transactionId = transactionMsgFMDTO.getTestId();
            }
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
        } else {
            transactionId = transactionMsgFMDTO.getId();
//            checkUtils.checkTransactionIdExist(transactionId);
            if (!StringUtils.isNotBlank(transactionId)){
                throw new FmException(FMResultEnum.TransactionIdNoExist);
            }
        }
        /**
         * id???TransactionId
         * ??????transactionMsgFMDTO
         */

        TransactionMsg transactionMsg = new TransactionMsg();
        transactionMsg.setTransactionId(transactionId);
        transactionMsg.setCode(transactionMsgFMDTO.getCode());
        transactionMsg.setCount(fmTotalCount);
        transactionMsg.setAmount(fmTotalAmount);
        transactionMsg.setBatchCount(Integer.valueOf(transactionMsgFMDTO.getBatchCount()));
        transactionMsg.setFixedValue(Integer.valueOf(transactionMsgFMDTO.getFixedValue()));
        transactionMsg.setSingleWeight(Integer.valueOf(transactionMsgFMDTO.getSingleWeight()));
        transactionMsg.setRegionType(transactionMsgFMDTO.getRegionType());
        transactionMsg.setLabelRu(transactionMsgFMDTO.getLabelRu());
        transactionMsg.setDmMsg(transactionMsgFMDTO.getDmMsg());
        transactionMsg.setFrankMachineId(transactionMsgFMDTO.getFrankMachineId());
        transactionMsg.setStatus(status);
        transactionMsg.setCreatedTime(new Date());
        this.createTransactionMsg(transactionMsg);
        log.info("transactionMsg???????????? id = " + transactionMsg.getId());

        return transactionId;
    }

    /**
     * ????????????????????????dmMsgDetail?????????????????????????????????
     * transaction??????????????????transactionid???????????????dm_msg?????????
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????dm_msg??????????????????????????????????????????
     *
     * @param transactionId
     * @return
     */
    @Override
    public DmMsgDetail getDmMsgDetailAfterFinishJob(String transactionId, boolean needProductPrintCount ) {
        List<TransactionMsg> transactionMsgList = selectByTransactionId(transactionId);
        return this.getDmMsgDetail(transactionMsgList, true, needProductPrintCount);
    }

    @Override
    public void deleteTransactionMsgBySchedule() {
        Query query = new Query();
        Criteria criteria = new Criteria();
        final SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );

        Date date = new Date();
        Format format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = df.parse(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime( date );
        cal.add( GregorianCalendar.MONTH, -6 );
        criteria.and("created_time").lte(cal.getTime());
        query.addCriteria(criteria);
        /*List<TransactionMsg> list =  mongoTemplate.find(query,TransactionMsg.class);
        log.info("list.size() = " + list.size());
        list.stream().forEach(s->{
            log.info("id = " + s.getId() +" time = " + DateUtil.formatDate(s.getCreatedTime()));
        });*/
        mongoTemplate.remove(query,"rcs_transaction_msg");
    }

    @Override
    public void batchCreate() {
        List<TransactionMsg> list = new ArrayList<TransactionMsg>();
//        for (Long i = 101L; i < 120; i++){
//                for (Long i = 100010140L; i < 100010240; i++){
//        for (int i = 200000001; i < 200010001; i++){
//        for (Long i = 200001020L; i < 200001030; i++){

        for (Long i = 10L; i < 15; i++){
            TransactionMsg transactionMsg = new TransactionMsg();
            transactionMsg.setId(i);
            transactionMsg.setTransactionId("0000cffcc-c277-4145-8b42-694114f54601");
            transactionMsg.setFrankMachineId("PM100200");
            transactionMsg.setCode("2100");
            transactionMsg.setCount(668L);
            transactionMsg.setAmount(1000L);
            transactionMsg.setDmMsg("01PM64313100022032210020000000556000800021000001000001770020");
            transactionMsg.setStatus("2");
            transactionMsg.setCreatedTime(new Date());
            list.add(transactionMsg);
//            if (i != 0 && i %100000 == 0){
            if (i != 0 && i %2 == 0){
                System.out.println("??????" + " " + i + "???");
                this.mongoTemplate.insertAll(list);
                list.clear();
            }
        }
    }
}
