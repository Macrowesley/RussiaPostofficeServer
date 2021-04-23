package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.rcs.dto.manager.ForeseenDTO;
import cc.mrbird.febs.rcs.dto.manager.ForeseenProductDTO;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ForeseensPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xB5;

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
            unsigned char type;					//0xB5
            unsigned char acnum[6];             //机器表头号
            unsigned char content[?];			//加密后内容:
            版本内容（长度3）+ FrankMachineId() + TaxVersion() + PostOffice(6) + userId() + ContractId() + ContractNum(int 4) + TotalCount(int 4) + MailVal(8 double 类型 最后2位是小数)
             + 产品数组（int 4） +  【productCode() + count(int 4) + weight(double 8) + amount(double 8)】 一直拼接
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))Foreseens, *Foreseens;
         */
        int pos = TYPE_LEN;

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //todo 解析什么
        // 产品列表？ 总金额 机器其他信息


        ForeseenDTO foreseen = new ForeseenDTO();
        foreseen.setId("");
        foreseen.setPostOffice("");
        foreseen.setUserId("");
        foreseen.setContractId("");
        foreseen.setContractNum(0);
        foreseen.setTotalCount(0);
        foreseen.setProducts(new ForeseenProductDTO[]{new ForeseenProductDTO()});
        foreseen.setFrankMachineId("");
        foreseen.setTaxVersion("");
        foreseen.setMailVal(0.0D);

        //需要知道哪些数据是机器能提供的

        serviceManageCenter.foreseens(foreseen);


        //返回 todo 返回需要写清楚点
        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char head;				 	 //0xB5
         unsigned char content;				     //加密内容: result(0 失败 1 成功) + foreseenId（？）
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))ForeseensResult, *ForeseensResult;
         */
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
