package cc.mrbird.febs.common.netty.protocol.machine.result;

import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@NoArgsConstructor
@Component
public class OpenSSHResultPortocol extends MachineToServiceProtocol {
    public static final byte PROTOCOL_TYPE = (byte) 0xB1;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //SSH结果长度
    private static final int REQ_SSH_RES_LEN = 1;

    public static OpenSSHResultPortocol openSSHResultPortocol;

    private static final String OPERATION_NAME = "OpenSSHResultPortocol";

    @PostConstruct
    public void init(){
        openSSHResultPortocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return openSSHResultPortocol;
    }

    /**
     * 获取协议类型
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

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
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        /*
        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length[4];				//
            unsigned char type;					//0xB1
            unsigned char  operateID[2];
            unsigned char acnum[6];             //机器表头号
            unsigned char result;				//0x00 失败  0x01 成功
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))ssh, *ssh;
         */
        int pos = getBeginPos();

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //ssh结果
        String res = bytes[pos] == 0x01 ?"成功":"失败";

        log.info("{}机器的ssh打开结果：{}", acnum, res);

        //返回
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
