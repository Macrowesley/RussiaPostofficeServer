package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.asu.entity.manager.Foreseen;
import cc.mrbird.febs.asu.entity.manager.ForeseenProduct;
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
    public byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        /*
        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length;				//0x0 ?
            unsigned char type;					//0xB5
            unsigned char acnum[6];             //机器表头号
            unsigned char content[?];			//加密后内容: 版本内容（长度3） + todo 机器信息（）？
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))ssh, *ssh;
         */
        int pos = TYPE_LEN;

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //todo 解析什么
        // 产品列表？ 总金额 机器其他信息


        Foreseen foreseen = new Foreseen();
        foreseen.setId("");
        foreseen.setPostOffice("");
        foreseen.setUserId("");
        foreseen.setContractId("");
        foreseen.setContractNum(0);
        foreseen.setTotalCount(0);
        foreseen.setProducts(new ForeseenProduct[]{new ForeseenProduct()});
        foreseen.setFrankMachineId("");
        foreseen.setTaxVersion("");
        foreseen.setMailVal(0.0D);

        //需要知道哪些数据是机器能提供的

        serviceManageCenter.foreseens(foreseen);


        //返回 todo 返回需要写清楚点
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
