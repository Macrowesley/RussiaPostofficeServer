package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CancelJobFMDTO;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionFMDTO;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.common.utils.MoneyUtils;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.service.ITransactionMsgService;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Slf4j
@NoArgsConstructor
@Component
public class TransactionsPortocol extends MachineToServiceProtocol {

    public static final byte PROTOCOL_TYPE = (byte) 0xB6;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    private static final String OPERATION_NAME = "TransactionsPortocol";

    @Autowired
    ITransactionMsgService dmMsgService;

    public static TransactionsPortocol transactionsPortocol;

    @PostConstruct
    public void init(){
        this.transactionsPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return transactionsPortocol;
    }

    /**
     * 获取协议类型
     *
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    /**
     * 解析并返回结果流
     *
     * @param bytes
     * @param ctx
     * @return
     */
    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        String version = null;
        try {
            /*
            typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[2];			//
                unsigned char type;					//0xB6
                unsigned char  operateID[2];
                unsigned char acnum[6];             //机器表头号
                unsigned char version[3];           //版本号
                unsigned char content[?];			//加密后内容: TransactionFMDTO的json
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))Transactions, *Transactions;
             */
            log.info("机器开始 transaction");

            //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
            String key = ctx.channel().id().toString() + "_" + OPERATION_NAME;
            if (transactionsPortocol.redisService.hasKey(key)) {
                return getOverTimeResult(version, ctx, key, FMResultEnum.Overtime.getCode());
            } else {
                log.info("channelId={}的操作记录放入redis", key);
                transactionsPortocol.redisService.set(key, "wait", WAIT_TIME);
            }

            int pos = getBeginPos();

            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //版本号
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;

            switch (version) {
                case FebsConstant.FmVersion1:
                    TransactionFMDTO transactionFMDTO = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, TransactionFMDTO.class);
                    log.info("解析得到的对象：TransactionFMDTO={}", transactionFMDTO.toString());

                    if (StringUtils.isEmpty(transactionFMDTO.getContractCode())
                            || StringUtils.isEmpty(transactionFMDTO.getFrankMachineId())
                            || StringUtils.isEmpty(transactionFMDTO.getPostOffice())
                            || StringUtils.isEmpty(transactionFMDTO.getTaxVersion())
                            || StringUtils.isEmpty(transactionFMDTO.getForeseenId())
                            || StringUtils.isEmpty(transactionFMDTO.getId())) {
                        return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.SomeInfoIsEmpty.getCode());
                    }

                    //创建UUID
                    /*String transactionId = AESUtils.createUUID();
                    transactionFMDTO.setId(transactionId);*/
                    String transactionId = transactionFMDTO.getId();

                    Contract dbContract = null;
                    //取消订单
                    if (Long.valueOf(transactionFMDTO.getAmount()) == 0) {
                        CancelJobFMDTO cancelJobFMDTO = new CancelJobFMDTO();
                        cancelJobFMDTO.setFrankMachineId(transactionFMDTO.getFrankMachineId());
                        cancelJobFMDTO.setForeseenId(transactionFMDTO.getForeseenId());
                        cancelJobFMDTO.setCancelMsgCode(transactionFMDTO.getCancelMsgCode());
                        cancelJobFMDTO.setContractCode(transactionFMDTO.getContractCode());
                        dbContract = transactionsPortocol.serviceManageCenter.cancelJob(cancelJobFMDTO);
                    } else {
                        //处理transaction
                        //数据库得到具体的dmMsg信息
                        DmMsgDetail dmMsgDetail = transactionsPortocol.dmMsgService.getDmMsgDetailAfterFinishJob(transactionId);
                        transactionFMDTO.setAmount(dmMsgDetail.getActualAmount());
                        transactionFMDTO.setCount(dmMsgDetail.getActualCount());
                        transactionFMDTO.setFranks(dmMsgDetail.getFranks());
                        dbContract = transactionsPortocol.serviceManageCenter.transactions(transactionFMDTO);
                    }
                    return getSuccessResult(version, ctx, transactionId, dbContract);
                default:
                    return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }

        }catch (FmException e){
            log.error(OPERATION_NAME + " FmException info = " + e.getMessage());
            if (-1 != e.getCode()) {
                return getErrorResult(ctx, version, OPERATION_NAME, e.getCode());
            }else{
                return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.DefaultError.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(OPERATION_NAME + " error info = " + e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME);
        } finally {
            log.info("机器结束 transaction");
        }
    }

    private byte[] getSuccessResult(String version, ChannelHandlerContext ctx, String transactionId, Contract contract) throws Exception {
        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char type;				 	 //0xB6
         unsigned char  operateID[2];
         unsigned char content;				     //加密内容: result(1 成功) + version + transactionId（36）+ consolidate(8 分为单位) + current(8 分为单位)
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))ForeseensResult, *ForeseensResult;
         */
        String responseData = FMResultEnum.SUCCESS.getSuccessCode() + version + transactionId + MoneyUtils.changeY2F(contract.getConsolidate()) + MoneyUtils.changeY2F(contract.getCurrent());
        String tempKey = transactionsPortocol.tempKeyUtils.getTempKey(ctx);
        String resEntryctContent = AESUtils.encrypt(responseData, tempKey);
        log.info("transaction 协议：原始数据：" + responseData + " 密钥：" + tempKey + " 加密后数据：" + resEntryctContent);
        return getWriteContent(BaseTypeUtils.stringToByte(resEntryctContent, BaseTypeUtils.UTF8));
    }
}
