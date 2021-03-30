package cc.mrbird.febs.common.netty.protocol.machine;

import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CancelJobPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xB7;

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
            unsigned char type;					//0xB7
            unsigned char acnum[6];             //机器表头号
            unsigned char content[?];			//加密后内容: 版本内容（长度3） + frankMachineId(?) + foreseenId(?) + cancelMessage（?）
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))CancelJob, *CancelJob;
         */
        int pos = TYPE_LEN;

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //todo 解析什么
        String frankMachineId = "";
        String foreseenId = "";
        String cancelMessage = "";

        serviceManageCenter.cancelJob(frankMachineId,foreseenId,cancelMessage);


        //返回 todo 返回需要写清楚点
        /**
         typedef  struct{
         unsigned char length;				     //一个字节
         unsigned char head;				 	 //0xB7
         unsigned char content;				     //加密内容: result(0 失败 1 成功)
         unsigned char check;				     //校验位
         unsigned char tail;					 //0xD0
         }__attribute__((packed))CancelJobResult, *CancelJobResult;
         */
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
