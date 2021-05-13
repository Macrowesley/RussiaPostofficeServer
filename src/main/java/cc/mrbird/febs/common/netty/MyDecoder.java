package cc.mrbird.febs.common.netty;

import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MyDecoder extends ByteToMessageDecoder {

    /**
     * <pre>
     * 协议开始的标准第一个字节，表示长度
     * AA 长度 类型 内容 验证码 D0
     * </pre>
     */
    public final int BASE_LENGTH = 1 + BaseProtocol.REQUEST_LENGTH_LEN;
    private final byte HEAD_BYTE = (byte) 0xAA;
    private final int MAX_LEN = 2048;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buffer, List<Object> list) throws Exception {
//        log.info("decode 字节流 所在的通道是 " + channelHandlerContext.toString());
//        log.info("接收到的数据长度：{}",buffer.readableBytes());

        /*test(buffer, list);
        boolean isTest = true;
        if (isTest){
            return;
        }*/

        // 可读长度必须大于基本长度
        if (buffer.readableBytes() >= BASE_LENGTH && buffer.isReadable()) {
            long time1 = System.currentTimeMillis();
            // 防止socket字节流攻击
            // 防止，客户端传来的数据过大
            // 因为，太大的数据，是不合理的
            if (buffer.readableBytes() > MAX_LEN) {
                log.error("数据包过大，不合理");
                buffer.skipBytes(buffer.readableBytes());
                return;
            }

            /*长度至少为9
                    第一个字节为AA,否则跳过
            第二个字节长度，然后得到长度后面的数据的验证字节是否OK
            OK的话，保存，不OK的话，跳过*/

            //开始读的位置
            int beginReaderIndex = buffer.readerIndex();

            byte headByte = buffer.readByte();
//            log.info("解析头部字节内容" + BaseTypeUtils.bytesToHexString(new byte[]{headByte}));
            if(headByte != HEAD_BYTE){
                log.info("头字节不对，跳过" + BaseTypeUtils.bytesToHexString(new byte[]{headByte}));
                return;
            }

            //读长度
           /* byte lengthByte = buffer.readByte();
            int length = BaseTypeUtils.byte2Int(lengthByte);
            //log.info("长度 = " + length + "  内容 = " + BaseTypeUtils.bytesToHexString(new byte[]{lengthByte}));
            */
            byte[] lengthByte = new byte[BaseProtocol.REQUEST_LENGTH_LEN];
            buffer.readBytes(lengthByte);
            int length = BaseTypeUtils.ByteArray2IntConsOnLenght(lengthByte);
            log.info("长度 = " + length + "  内容 = " + BaseTypeUtils.bytesToHexString(lengthByte));

            // 判断请求数据包数据是否到齐
            if (length > MAX_LEN) {
                log.error("读取的长度 = {}，太大了，不合理，跳过这个字节", length);
                return;
            }
            if (buffer.readableBytes() < length) {
                // 还原读指针
                log.info("长度不够，还原读指针位置");
                buffer.readerIndex(beginReaderIndex);
                return;
            }

            // 读取data数据
            byte[] data = new byte[length];
            buffer.readBytes(data);
            if (!BaseTypeUtils.checkChkSum(data, data.length - 2)){
                log.error("验证不成功，跳过这些数据");
                log.info("长度 = " + length + "  长度内容 = " + BaseTypeUtils.bytesToHexString(lengthByte));
                log.error("长度 = " + length + " 从第4个字节开始 data = "  + BaseTypeUtils.bytesToHexString(data));
                return;
            }
//            log.info("验证成功 data = "  + BaseTypeUtils.bytesToHexString(data));
            SocketData socketData = new SocketData();
            socketData.setContent(data);
            list.add(socketData);

            log.info("decode 总耗时：" + (System.currentTimeMillis() - time1));
        }

        //定长 解析没有问题
        /*int frameLength = 10;
        if (buffer.readableBytes() < frameLength){
            return;
        }
        byte[] data = new byte[frameLength];
        buffer.readRetainedSlice(frameLength).readBytes(data);
        log.info("decoder里面，pos = " + buffer.readerIndex() + " data 长度 = " + data.length + " 内容 = " + BaseTypeUtils.bytesToHexString(data));
        SocketData socketData = new SocketData();
        socketData.setContent(data);
        list.add(socketData);

        ReferenceCountUtil.release(buffer);*/
    }
    @Deprecated
    private void test(ByteBuf buffer, List<Object> list){
        if (buffer.readableBytes() >= BASE_LENGTH && buffer.isReadable()) {
            long time1 = System.currentTimeMillis();
            // 防止socket字节流攻击
            // 防止，客户端传来的数据过大
            // 因为，太大的数据，是不合理的
            if (buffer.readableBytes() > 2048) {
                log.error("数据包过大，不合理");
                buffer.skipBytes(buffer.readableBytes());
                return;
            }

            /*长度至少为9
                    第一个字节为AA,否则跳过
            第二个字节长度，然后得到长度后面的数据的验证字节是否OK
            OK的话，保存，不OK的话，跳过*/

            //开始读的位置
            int beginReaderIndex = buffer.readerIndex();

            byte headByte = buffer.readByte();
//            log.info("解析头部字节内容" + BaseTypeUtils.bytesToHexString(new byte[]{headByte}));

            //读长度
            byte lengthByte = buffer.readByte();
            int length = BaseTypeUtils.byte2Int(lengthByte);
//            log.info("长度 = " + length + "  内容 = " + BaseTypeUtils.bytesToHexString(new byte[]{lengthByte}));

            /*byte[] lengthBytes = new byte[BaseProtocol.REQUEST_LENGTH_LEN];
            buffer.readBytes(lengthBytes);
            int length = BaseTypeUtils.ByteArray2IntConsOnLenght(lengthBytes);
            log.info("长度 = " + length + "  内容 = " + BaseTypeUtils.bytesToHexString(lengthBytes));*/

            // 读取data数据
            byte[] data = new byte[length];
            buffer.readBytes(data);

            log.info("得到数据 len={}, data = {}", length, BaseTypeUtils.bytesToHexString(data));
            SocketData socketData = new SocketData();
            socketData.setContent(data);
            list.add(socketData);
        }
    }
}
