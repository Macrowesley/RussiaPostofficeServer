package cc.mrbird.febs.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class AESUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return org.apache.commons.codec.binary.Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception ex) {
//            ex.printStackTrace();
            log.error("AES 加密操作 失败：" + ex.getMessage());
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, String key) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));

        //执行操作
        byte[] result = cipher.doFinal(org.apache.commons.codec.binary.Base64.decodeBase64(content));

        return new String(result, "utf-8");
    }

    /**
     * 生成加密秘钥
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    private static SecretKeySpec getSecretKey(final String key) throws UnsupportedEncodingException {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);

            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(key.getBytes()));

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(Arrays.copyOf(key.getBytes("utf-8"), 16), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
//            ex.printStackTrace();
            log.error("生成加密秘钥 失败：" + ex.getMessage());
        } catch (Exception e){
            log.error("生成加密秘钥 失败：" + e.getMessage());
        }

        return null;
    }

    /**
     * 创建任意位的密钥
     *
     * @return
     */
    public static String createKey(int KeyLength) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer Keysb = new StringBuffer();
        for (int i = 0; i < KeyLength; i++) {
            int number = random.nextInt(base.length());
            Keysb.append(base.charAt(number));
        }
        return Keysb.toString();
    }

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    /**
     * 生成短8位的uuid
     *
     * @param
     * @return
     */
    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }


    /**
     * 获取16位的uuid
     * @return
     */
    public static String generateUUID16Len() {
        String uuid= UUID.randomUUID().toString().replace("-","");
        StringBuffer Keysb = new StringBuffer();
        Random random = new Random();
        int number;
        for (int i = 0; i < 16; i++) {
            number = random.nextInt(uuid.length());
            Keysb.append(uuid.charAt(number));
        }
        return Keysb.toString();
    }


    public static String createUUID() {
        return UUID.randomUUID().toString();
    }


    public static void main(String[] args) throws Exception {
//        test1();
//        test2();
//        test3();
//          test4();
//        test5();
//        test6();
        test7();
    }

    private static void test7() {
        String uuid = createUUID();
        System.out.println(uuid);
    }

    private static void test6() {
        String dectryptContent = "00110000004900012345";
        int pos = 3 ;
        String res =  dectryptContent.substring(pos, pos+1);
        pos += 1;

        //机器订单ID
        String orderId = dectryptContent.substring(pos, pos + 8);
        pos += 8;

        //注资金额
        String amount = dectryptContent.substring(pos, pos + 8);
        pos += 8;
        System.out.println(res + ", " + orderId + " , " + amount);
    }

    private static void test5() throws Exception {
        String uuid = generateUUID16Len();
        String tempKey = AESUtils.createKey(16);
        String entryptContent = AESUtils.encrypt(tempKey, uuid);
        int RES_ENCRYPT_LEN = 44;
        byte[] encryptBytes = new byte[RES_ENCRYPT_LEN];

        encryptBytes = BaseTypeUtils.stringToByte(entryptContent, RES_ENCRYPT_LEN, BaseTypeUtils.UTF8);

        String res = BaseTypeUtils.byteToString(encryptBytes, BaseTypeUtils.UTF8);
        System.out.println("要加密的内容：" + tempKey);
        System.out.println("密钥="+ uuid);
        System.out.println("原始文本="+entryptContent + " 变成字节后最终还原的文本="+ res);
        System.out.println(" 最后解密的内容=" + decrypt(res, uuid));
    }

    private static void test4() {
        int n = 0;
        long t = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {//1000000
            long t1 = System.currentTimeMillis();
            String key = generateUUID16Len();
            String tempKey = AESUtils.createKey(16);
            String res = AESUtils.encrypt(tempKey, key);
//            System.out.println("key = " + key + " tempKey=" +tempKey+ " res长度 = " + res.length() + " res = " + res);
            if (res.length() != 44) {
                System.out.println("有例外");
            } else {
                n++;
            }

        }
        System.out.println("n = " + n);
        System.out.println("耗时：" + (System.currentTimeMillis() - t));

    }

    private static void test3() throws Exception {
        String content = "123456";
        String key = "111111";


        String entryptContent = AESUtils.encrypt(content, key);
        System.out.println("加密前内容：" + content);
        System.out.println("加密后的内容：" + entryptContent);

        entryptContent = "KrRPAWzCOvHEgK3jjtVDHA==";
        key = "SppTi3alaNNJoTDf";

        entryptContent = "wflYk5saIuyUdCjuOkX6NQ==";
        key = "1111111111111111";
        String res = AESUtils.decrypt(entryptContent, key);
        System.out.println("解密后内容：" + res);
    }

    private static void test2() {
        int n = 0;
        long t = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            long t1 = System.currentTimeMillis();
            String key = createUUID();
//        System.out.println("耗时：" + (System.currentTimeMillis() - t1));
            String tempKey = createKey(8);
            String res = AESUtils.encrypt(tempKey, key);
//        System.out.println("key = " + key + " tempKey=" +tempKey+ " res长度 = " + res.length() + " res = " + res);
            if (res.length() != 24) {
                System.out.println("有例外");
            } else {
                n++;
            }

        }
        System.out.println("n = " + n);
        System.out.println("耗时：" + (System.currentTimeMillis() - t));

    }

    private static void test1() throws Exception {

        /*long time = System.currentTimeMillis();
        String plainText = "m=1&n=10&t=1&d=1232211312";
        //注意key不为16位,会自动补充0
        String key = "kkkkkkkkkkkkkkkk";
        key = createKey(16);
        key = "Jdr30qPBgNqmuCNZ";

        //返回的秘钥
        byte[] keyByteArr = BaseTypeUtils.stringToByte(key, 16, BaseTypeUtils.UTF8);
        ;
        System.out.println("bytes:" + BaseTypeUtils.bytesToHexString(keyByteArr));
        System.out.println("key:" + key);
        System.out.println("plainText:" + plainText);


        String encryptionText = AESUtils.encrypt(plainText, key);
        System.out.println("加密后的密码(encryptionText):" + encryptionText);

        System.out.println("解密(decrypt):" + AESUtils.decrypt(encryptionText, key));
        //2147483647
        System.out.println("耗时：" + (System.currentTimeMillis() - time));
        System.out.println(System.currentTimeMillis() / 1000);*/
        //U2FsdGVkX1/6eZPBPkKKcoFqwgghk3jSJCSSLrnWHR3yjKH/xFQmfH/anmeXAAz1CbL0Wv+3QMsUJK+eYYFFGQ==
        //tCWsMGbAF0uKRkVoFlmL6ivM9rniCaXPk1aBLnTJKHaOqbS1G73MlM3TpFgOP3OB

//        key=SppTi3alaNNJoTDf, content=93fi3Z5Er7977bY ESPtqaN1Ew35zGY/nVGqBJ3TfpY=
        String key = "ihstP2vD34eY8w7y";
        key = "SppTi3alaNNJoTDf";
        String oldContent = "";
        String content = "93fi3Z5Er7977bY+ESPtqQd+ecefrg5FU1mRdIF93IQ=";
//                 93fi3Z5Er7977bY ESPtqaN1Ew35zGY/nVGqBJ3TfpY=
        content = "NlWS+QNM8Bq4xxRGxkvQZt5NciT0/MbWBO8pZqcOok8=";
//        System.out.println("加密后的密码(encryptionText):" +  AESUtils.encrypt(oldContent, key));
        System.out.println("解密(decrypt):" + AESUtils.decrypt(content, key));
//        93fi3Z5Er7977bY+ESPtqQd+ecefrg5FU1mRdIF93IQ=
//        93fi3%5A5Er7%3977bY%2BESPtqQd%2Becefrg5FU1mRdIF%393IQ%3D
        //93fi3Z5Er7977bY%2BESPtqQd%2Becefrg5FU1mRdIF93IQ%3D

        String c = URLEncoder.encode(content, "utf-8");
        System.out.println("URL " + content);
        System.out.println("URLEncoder.encode:" + c);
        System.out.println("URLDecoder.decode:" + URLDecoder.decode("93fi3%5A5Er7%3977bY%2BESPtqQd%2Becefrg5FU1mRdIF%393IQ%3D", "utf-8"));
    }


}
