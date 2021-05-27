package cc.mrbird.febs.common.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 各基础类型与byte之间的转换
 *
 * @author shanl
 */
public class BaseTypeUtils {

    public static Charset UTF8 = Charset.forName("UTF-8");
    public static Charset GBK = Charset.forName("GBK");
    public static Charset GB2312 = Charset.forName("GB2312");
    public static Charset ISO = Charset.forName("ISO-8859-1");


    /**
     * 字符串到字节数组转换
     *
     * @param s       字符串。
     * @param charset 字符编码
     * @return 字符串按相应字符编码编码后的字节数组。
     */
    public static byte[] stringToByte(String s, Charset charset) {
        try {
            return s.getBytes(charset);
        } catch (Exception e) {
            System.out.println("stringToByte error :" + e.getMessage());
            return new byte[]{0x00,0x00};
        }
    }

    /**
     * 字符串到字节数组转换
     *
     * @param s       字符串。
     * @param charset 字符编码
     * @return 字符串按相应字符编码编码后的字节数组。
     */
    public static byte[] stringToByte(String s, int len, Charset charset) {
        byte[] res = new byte[len];
        byte[] strBytes = s.getBytes(charset);
        int minLen = Math.min(res.length, strBytes.length);
        for (int i = 0; i < minLen; i++) {
            res[i] = strBytes[i];
        }
//        LogUtils.log("新 byte 长度 = " + res.length);
        return res;
    }

    /**
     * 字节数组带字符串的转换
     *
     * @param b       字符串按指定编码转换的字节数组。
     * @param charset 字符编码。
     * @return 字符串。
     */
    public static String byteToString(byte[] b, Charset charset) {
        String res = new String(b, charset);
//        LogUtils.error("测试1",bytesToHexString(b) + " res = "+ res);
        res = res.replace((char)0, (char)' ');
//        LogUtils.error("测试2",bytesToHexString(b) + " res = "+ res);
        return res.trim();
    }

    public static String byteToString(byte[] b, int offset, int len, Charset charset) {
        return new String(b, offset, len, charset);
    }

    public static short[] bytesToShort(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        short[] shorts = new short[bytes.length / 2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        return shorts;
    }

    public static byte[] shortToBytes(short[] shorts) {
        if (shorts == null) {
            return null;
        }
        byte[] bytes = new byte[shorts.length * 2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shorts);

        return bytes;
    }

    public static byte[] shortToByteArray(short[] src) {
        if (src == null) {
            return null;
        }
        int count = src.length;
        byte[] dest = new byte[count << 1];

        int pos = 0;
        for (int i = 0; i < count; i++) {
//            dest[i * 2] = (byte) (src[i] >> 8);
//            dest[i * 2 + 1] = (byte) (src[i] >> 0);
            byte height = (byte) (src[i] >> 8);
            if (height == 0x00) {
                dest[pos] = (byte) (src[i] >> 0);
                pos++;
            } else {
                dest[pos] = (byte) (src[i] >> 8);
                pos++;
                dest[pos] = (byte) (src[i] >> 0);
                pos++;
            }
        }

        return dest;
    }

    public static short[] byteArrayToShort(byte[] src) {

        int count = src.length;
        short[] dest = new short[count];

        int pos = 0;
        for (int i = 0; i < count; i++) {
//            dest[i * 2] = (byte) (src[i] >> 8);
//            dest[i * 2 + 1] = (byte) (src[i] >> 0);
            dest[i] = (short) (((0x00 & 0xff) << 8) | (src[i] & 0xff));
        }

        return dest;
    }


    /**
     * 将short转成byte[2]
     *
     * @param a
     * @return
     */
    public static byte[] short2Byte(short a) {
        byte[] b = new byte[2];

        b[0] = (byte) (a >> 8);
        b[1] = (byte) (a);

        return b;
    }

    /**
     * 将short转成byte[2]
     *
     * @param a
     * @param b
     * @param offset b中的偏移量
     */
    public static void short2Byte(short a, byte[] b, int offset) {
        b[offset] = (byte) (a >> 8);
        b[offset + 1] = (byte) (a);
    }

    /**
     * 将byte[2]转换成short
     *
     * @param b
     * @return
     */
    public static short byte2Short(byte[] b) {
        return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
    }

    /**
     * 将byte[2]转换成short
     *
     * @param b
     * @param offset
     * @return
     */
    public static short byte2Short(byte[] b, int offset) {
        return (short) (((b[offset] & 0xff) << 8) | (b[offset + 1] & 0xff));
    }

    /**
     * byte[8]转long
     *
     * @param b
     * @return
     */
 /*   public static long byte2Long(byte[] bytes, int offset, int dateLen) {

        byte[] b = new byte[dateLen];
        System.arraycopy(bytes, offset, b, 0, dateLen);
//        LogUtils.log("byte2Long long的byte内容：" + bytesToHexString(b));
        long result = (long) ((b[0] & 0xFF)
                | ((b[1] & 0xFF) << 8)
                | ((b[2] & 0xFF) << 16)
                | ((b[3] & 0xFF) << 24)
                | ((b[4] & 0xFF) << x32)
                | ((b[5] & 0xFF) << 40)
                | ((b[6] & 0xFF) << 48)
                | ((b[7] & 0xFF) << 56));
        LogUtils.log("byte2Long  byte数组 = " + BaseTypeUtils.bytesToHexString(b) + " 转换后的long = " + result);

        return result;
    }


    public static byte[] long2Bytes(long i) {
        *//*byte[] result = new byte[8];
        result[7] = (byte) ((i >> 56) & 0xFF);
        result[6] = (byte) ((i >> 48) & 0xFF);
        result[5] = (byte) ((i >> 40) & 0xFF);
        result[4] = (byte) ((i >> x32) & 0xFF);
        result[3] = (byte) ((i >> 24) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        LogUtils.log("long2Bytes 内容：" + bytesToHexString(result));
        return result;
        *//*
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, i);
//        LogUtils.log("long2Bytes 内容：" + bytesToHexString(buffer.array()));
        return buffer.array();
    }*/
    public static long byte2Long(byte[] bytes, int offset, int dateLen) {
        if (bytes == null) {
            return 0;
        }
        byte[] b = new byte[dateLen];
        System.arraycopy(bytes, offset, b, 0, dateLen);
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (b[7 - ix] & 0xff);
        }
        //LogUtils.log("byte2Long  byte数组 = " + BaseTypeUtils.bytesToHexString(b) + " 转换后的long = " + num);

        return num;
    }

    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[7 - ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static long byte2LongNormal(byte[] bytes, int offset, int dateLen) {
        byte[] b = new byte[dateLen];
        System.arraycopy(bytes, offset, b, 0, dateLen);
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (b[ix] & 0xff);
        }
        //LogUtils.log("byte2Long  byte数组 = " + BaseTypeUtils.bytesToHexString(b) + " 转换后的long = " + num);

        return num;
    }

    public static byte[] long2BytesNormal(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }




    /**
     * 正序
     * @return
     */
    public static int byteArray2IntPos(byte[] bytes, int offset, int dateLen) {
        byte[] b = new byte[dateLen];
        System.arraycopy(bytes, offset, b, 0, dateLen);
        return byteArray2IntPos(b);
    }
    /**
     * 正序
     * @param b
     * @return
     */
    public static int byteArray2IntPos(byte[] b) {
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i)); //int占4个字节（0，1，2，3）
        }
        return intValue;
    }
    /**
     * 正序
     * @param b
     * @return
     */
    public static byte[] int2ByteArraysPos(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte数组转int 反序
     *
     * @param b
     * @return
     */
    public static int byteArray2IntCons(byte[] bytes, int offset, int dateLen) {
        byte[] b = new byte[dateLen];
        System.arraycopy(bytes, offset, b, 0, dateLen);

        int result = (int) ((b[0] & 0xFF)
                | ((b[1] & 0xFF) << 8)
                | ((b[2] & 0xFF) << 16)
                | ((b[3] & 0xFF) << 24));
//        LogUtils.log("int的byte内容：\n" + bytesToHexString(b) + "int的值 = " + result);
        return result;
    }

    /**
     * 2个字节的长度转数字（反序）
     * @param b
     * @return
     */
    public static int ByteArray2IntConsOnLenght(byte[] b){
        int result = (int) ((b[0] & 0xFF)
                | ((b[1] & 0xFF) << 8));
        return result;
    }

    /**
     * 长度转2个字节 反序
     * @param i
     * @return
     */
    public static byte[] int2ByteArrayConsOnLenght(int i) {
        byte[] result = new byte[2];
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 反序
     * @param i
     * @return
     */
    public static byte[] int2ByteArrayCons(int i) {
        byte[] result = new byte[4];
        result[3] = (byte) ((i >> 24) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        return result;
    }

    public static int byte2Int(byte b){
        return b & 0xff;
    }



    /**
     * int 类型转换为byte 类型
     * 截取int类型的最后8位,与 0xff
     * @param x
     * @return
     */
    public static byte intToByte(int x){
        byte b =(byte) (x & 0xff);
        return b;
    }

    public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if ((bytes == null) || (bytes.length == 0) || (offset < 0) || (dateLen <= 0)) {
            return null;
        }
        if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
            return null;
        }

        String asciiStr = null;
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {
            asciiStr = new String(data, "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        return asciiStr;
    }

    public static String bytesToAscii(byte[] bytes, int dateLen) {
        return bytesToAscii(bytes, 0, dateLen);
    }

    public static String bytesToAscii(byte[] bytes) {
        return bytesToAscii(bytes, 0, bytes.length);
    }

    public static String bytesToHexString(byte[] bytes, int offset, int len) {
        if (bytes == null)
            return "null!";

        StringBuilder ret = new StringBuilder(2 * len);

        for (int i = 0; i < len; ++i) {
//            ret.append("第" + i + "个： 0x");
            int b = 0xF & bytes[(offset + i)] >> 4;
//            ret.append("0123456789abcdef".charAt(b));
//            b = 0xF & bytes[(offset + i)];
//            ret.append("0123456789abcdef".charAt(b));

            ret.append("0x"+"0123456789abcdef".charAt(b));
            b = 0xF & bytes[(offset + i)];
            ret.append("0123456789abcdef".charAt(b) + ",");

        }

        return ret.toString();
    }


    public static String bytesToHexString(byte[] bytes, int len) {
        return ((bytes == null) ? "null!" : bytesToHexString(bytes, 0, len));
    }

    public static String bytesToHexString(byte[] bytes) {
        return ((bytes == null) ? "null!" : bytesToHexString(bytes, bytes.length));
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    /**
     * 校验得到的字节流是否正确
     *
     * @param packBytes
     * @param len       校验位的位置
     * @param checkByte
     * @return
     */
    public static boolean checkChkSum(byte[] packBytes, int len) {
        try {
            int checkSum = 0;
            for (int i = 0; i < len; i++) {
                checkSum += packBytes[i];//计算和校验
            }
            checkSum &= 0xff; //取低八位
//            System.out.println("正确的检测结果：" + BaseTypeUtils.bytesToHexString(new byte[]{(byte) checkSum}));
            return (byte) checkSum == packBytes[len];//比对给到的数据真实性
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 对需要发送的字节流在结尾添加校验位
     *
     * @param packBytes
     * @return
     */
    public static byte[] makeCheckSum(byte[] packBytes) {
        int checkSum = 0;
        for (int i = 0; i < packBytes.length; i++) {
            checkSum += packBytes[i];//计算和校验
        }
        checkSum &= 0xff; //取低八位
        return new byte[]{(byte) checkSum};
        /*byte[] send = Arrays.copyOf(packBytes, packBytes.length + 1);
        send[send.length - 1] = (byte) checkSum;
        return send;*/
    }

    /**
     * 取得字节流中的指定范围内的字节流
     * @param bytes
     * @param pos
     * @param len
     * @return
     */
    public static byte[] getRangeBytes(byte[] bytes, int pos, int len) {
        byte[] newBytes = new byte[len];
        System.arraycopy(bytes, pos, newBytes, 0, len);
        return newBytes;
    }

    /**
     * 取得字节流中的指定范围内的字节流形成的字节流
     * @param bytes
     * @param pos
     * @param len
     * @return
     */
    public static String getRangeString(byte[] bytes, int pos, int len) {
        try {
            return BaseTypeUtils.byteToString(getRangeBytes(bytes,pos,len),UTF8);
        }catch (Exception e){
            return null;
        }
    }

    public static String getPatternContent(String pattern, String content){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        if (m.find()){
            return m.group(0);
        }else{
            return "";
        }

    }



    public static void main(String[] args) {
        int a = 161;
        byte[] bytes = int2ByteArrayConsOnLenght(a);
        int b = ByteArray2IntConsOnLenght(bytes);
        System.out.println("a = " + a + " b = " + b + " bytes = " + bytesToHexString(bytes));

        a = 300;
        bytes = int2ByteArrayConsOnLenght(a);
        b = ByteArray2IntConsOnLenght(bytes);
        System.out.println("a = " + a + " b = " + b + " bytes = " + bytesToHexString(bytes));

        /*byte[] arr = new byte[]{0x01,0x34,0x17, (byte) 0x8A};
        System.out.println(byteArray2IntPos(arr,0,4));
        System.out.println(bytesToHexString(int2ByteArraysPos(byteArray2IntPos(arr))));*/
    }
}
