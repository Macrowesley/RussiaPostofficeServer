package cc.mrbird.febs.common.netty.protocol.machine.result;

import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloseSSHResultPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xB2;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //SSH结果长度
    private static final int REQ_SSH_RES_LEN = 1;

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
            unsigned char length;				//0x09
            unsigned char type;					//0xB2
            unsigned char acnum[6];             //机器表头号
            unsigned char result;				//0x00 失败  0x01 成功
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))ssh, *ssh;
         */
        int pos = TYPE_LEN;

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //ssh结果
        String sshRes = BaseTypeUtils.byteToString(bytes, pos, REQ_SSH_RES_LEN, BaseTypeUtils.UTF8);

        log.info("{}机器的ssh打开结果：{}", acnum, sshRes.equals("1")? "成功":"失败");

        //返回
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
