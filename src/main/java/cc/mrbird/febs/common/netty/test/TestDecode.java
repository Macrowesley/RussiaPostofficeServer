package cc.mrbird.febs.common.netty.test;

import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TestDecode extends ByteToMessageDecoder {
    /**
     * Decode the from one {@link ByteBuf} to an other. This method will be called till either the input
     * {@link ByteBuf} has nothing to read when return from this method or till nothing was read from the input
     * {@link ByteBuf}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in  the {@link ByteBuf} from which to read data
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buffer, List<Object> list) throws Exception {
//        System.out.println("channelActive NettyServerHandler 对象是：" + this);
/*        byte[] lengthByte = new byte[BaseProtocol.REQUEST_LENGTH_LEN];
        buffer.readBytes(lengthByte);
        int length = BaseTypeUtils.ByteArray2IntConsOnLenght(lengthByte);

        byte[] data = new byte[length];
        buffer.readBytes(data);

        log.info("长度 = " + length + "  内容 = " + BaseTypeUtils.bytesToHexString(data));*/

        /*ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                        System.out.println("future.toString() = " + future.toString());
                        if(future.isSuccess()){
                            System.out.println("握手成功");

                            SSLSession ss =  ctx.pipeline().get(SslHandler.class).engine().getSession();
                            System.out.println("cipherSuite:"+ss.getCipherSuite());
                            X509Certificate cert = ss.getPeerCertificateChain()[0];
                            String info = null;
                            // 获得证书版本
                            info = String.valueOf(cert.getVersion());
                            System.out.println("证书版本:" + info);
                            // 获得证书序列号
                            info = cert.getSerialNumber().toString(16);
                            System.out.println("证书序列号:" + info);
                            // 获得证书有效期
                            Date beforedate = cert.getNotBefore();
                            info = new SimpleDateFormat("yyyy/MM/dd").format(beforedate);
                            System.out.println("证书生效日期:" + info);
                            Date afterdate = (Date) cert.getNotAfter();
                            info = new SimpleDateFormat("yyyy/MM/dd").format(afterdate);
                            System.out.println("证书失效日期:" + info);
                            // 获得证书主体信息
                            info = cert.getSubjectDN().getName();
                            System.out.println("证书拥有者:" + info);
                            // 获得证书颁发者信息
                            info = cert.getIssuerDN().getName();
                            System.out.println("证书颁发者:" + info);
                            // 获得证书签名算法名称
                            info = cert.getSigAlgName();
                            System.out.println("证书签名算法:" + info);

                        }else{
                            System.out.println("握手失败");
                        }
                    }
                });*/
    }
}
