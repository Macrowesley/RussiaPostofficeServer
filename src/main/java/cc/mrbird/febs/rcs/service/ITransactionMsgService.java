package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.netty.protocol.dto.TransactionMsgFMDTO;
import cc.mrbird.febs.rcs.dto.machine.DmMsgDetail;
import cc.mrbird.febs.rcs.entity.TransactionMsg;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 交易表 Service接口
 *
 * @author mrbird
 * @date 2021-04-17 14:45:17
 */
public interface ITransactionMsgService extends IService<TransactionMsg> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param transactionMsg transactionMsg
     * @return IPage<TransactionMsg>
     */
    IPage<TransactionMsg> findTransactionMsgs(QueryRequest request, TransactionMsg transactionMsg);

    /**
     * 查询（所有）
     *
     * @param transactionMsg transactionMsg
     * @return List<TransactionMsg>
     */
    List<TransactionMsg> findTransactionMsgs(TransactionMsg transactionMsg);

    /**
     * 新增
     *
     * @param transactionMsg transactionMsg
     */
    void createTransactionMsg(TransactionMsg transactionMsg);

    /**
     * 修改
     *
     * @param transactionMsg transactionMsg
     */
    void updateTransactionMsg(TransactionMsg transactionMsg);

    /**
     * 删除
     *
     * @param transactionMsg transactionMsg
     */
    void deleteTransactionMsg(TransactionMsg transactionMsg);

    /**
     * 根据transactionId得到打印任务中所有的dmMsg列表
     * @param transactionId
     * @return
     */
    List<TransactionMsg> selectByTransactionId(String transactionId);

    /**
     * 获取最新的msg
     * @param transactionId
     * @return
     */
    TransactionMsg getLastestMsg(String transactionId);
    /**
     * 判断是否有一样的记录
     * @param transactionMsgFMDTO
     * @return
     */
    boolean checkIsSameWithLastOne(TransactionMsgFMDTO transactionMsgFMDTO);
    /**
     * 根据列表信息，得到详情
     * @param msgList
     * @return
     */
    DmMsgDetail getDmMsgDetail(List<TransactionMsg> msgList, boolean needDmMsgList, boolean needProductPrintCount);

    /**
     * 机器开机的时候获取dmMsgDetail
     * 判断上一个批次是否结束
     *    结束了，返回null
     *    未结束：找到没有结束的那个批次，得到dm_msg等信息，保存这个dm_msg到数据库中，同时返回进度给机器
     */
    DmMsgDetail getDmMsgDetailOnFmStart(String transactionId, TransactionMsgFMDTO transactionMsgFMDTO);

    /**
     * 保存批次发送的内容,返回transactionId
     *
     * @param transactionMsgFMDTO
     */
    String saveMsg(TransactionMsgFMDTO transactionMsgFMDTO);

    /**
     * 完成任务后，得到dmMsgDetail，来构建给俄罗斯的内容
     * transaction结束后，根据transactionid得到所有的dm_msg信息，
     * 然后按时间顺序，一个批次前后记录相减，得到一个批次实际打印金额和重量，累计得到总金额和总件数，并得到dm_msg数组，拼接数据，发送给俄罗斯
     * @param transactionId
     * @return
     */
    DmMsgDetail getDmMsgDetailAfterFinishJob(String transactionId, boolean needProductPrintCount);

}
