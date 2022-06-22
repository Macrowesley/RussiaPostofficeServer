package cc.mrbird.febs.common.netty.protocol.machine.result;

import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateRemoteFileResultPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xBD;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //SSH结果长度
    private static final int REQ_RES_LEN = 1;

    //返回数据长度
    private static final int RES_DATA_LEN = 1;

    private static final String OPERATION_NAME = "UpdateRemoteFileResultPortocol";

    /**
     * 获取协议类型
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    /**
     * 获取协议名
     *
     * @return
     */
    @Override
    public String getProtocolName() {
        return OPERATION_NAME;
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
            //接收机器返回值
            typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[4];				//
                unsigned char type;					//0xBD
                unsigned char operateID[2];
                unsigned char acnum[6];             //机器表头号
                unsigned char result;				//0x00 失败  0x01 成功
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))updateFile, *updateFile;
         */
        int pos = getBeginPos();

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        log.info("bytes:"+BaseTypeUtils.bytesToHexString(bytes));
        //结果
        String res = bytes[pos] == 0x01 ?"成功":"失败";

        log.info("{}机器的文件更新结果：{}", acnum, res);

        //返回
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }

    /**
     * 获取实际操作的类
     *
     * @return
     */
    @Override
    public BaseProtocol getOperator() {
        return null;
    }
}
