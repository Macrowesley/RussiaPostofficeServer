package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.rcs.dto.manager.FrankDTO;
import cc.mrbird.febs.rcs.dto.manager.TransactionDTO;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TransactionsPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xB6;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;



    //返回数据长度
    private static final int RES_DATA_LEN = 1;

    /**
     * 获取协议类型
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
        /*
        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length;				//0x0 ?
            unsigned char type;					//0xB6
            unsigned char acnum[6];             //机器表头号
            unsigned char content[?];			//加密后内容: 版本内容（长度3） + ForeseenId() + PostOffice(6) + FrankMachineId() + ContractId() + StartDateTime() + StopDateTime +
                                                            UserId() + CreditVal(8 Double) + Amount(8 Double) + Count(int 4) + GraphId() + TaxVersion() + franksNum(4 数组数量) + Franks(定长)
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))Transactions, *Transactions;
         */
        int pos = TYPE_LEN;

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //todo 解析什么
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId("");
        transactionDTO.setForeseenId("");
        transactionDTO.setPostOffice("");
        transactionDTO.setFrankMachineId("");
        transactionDTO.setContractId("");
        transactionDTO.setContractNum(0);
        transactionDTO.setStartDateTime("");
        transactionDTO.setStopDateTime("");
        transactionDTO.setUserId("");
        transactionDTO.setCreditVal(0.0D);
        transactionDTO.setAmount(0.0D);
        transactionDTO.setCount(0);
        transactionDTO.setGraphId("");
        transactionDTO.setTaxVersion("");
        transactionDTO.setFranks(new FrankDTO[]{new FrankDTO(),new FrankDTO()});



        serviceManageCenter.transactions(transactionDTO);


        //返回 todo 返回需要写清楚点
        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char head;				 	 //0xB6
         unsigned char content;				     //加密内容: result(0 失败 1 成功) + transactionId（？）
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))TransactionsResult, *TransactionsResult;
         */
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
