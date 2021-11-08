package cc.mrbird.febs.common.utils.EcDsa;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Base64;

/**
 * @author korkin_ea
 * @since 18.10.2021.
 */
@Slf4j
/**
 * EcDsa算法签名验证
 * 配合俄罗斯使用
 */
public class DigitalSignatureTestHelper {
    public static final String SIGNATURE_ALGORITHM = "SHA1withECDSA";
    public static final String CURVE_NAME = "secp192r1";
    private static final String CHARSET_NAME = "UTF-8";

    static{
        try{
            Security.addProvider(new BouncyCastleProvider());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * generate signature in base64 string format
     * liu实现
     */
    public static String signatureForData(String data, PrivateKey privateKey){
        try {
            Signature ecdsaSign = Signature.getInstance(SIGNATURE_ALGORITHM);
            ecdsaSign.initSign(privateKey);
            ecdsaSign.update(data.getBytes());
            byte[] signature = ecdsaSign.sign();
            String sign = Base64.getEncoder().encodeToString(signature);
            return sign;
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            log.error("error while generating digital structure for message {}", data, e);
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String data, PublicKey publicKey, byte[] sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//        signature = Signature.getInstance("SHA1");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        return signature.verify(sign);
    }

    /**
     * generate keys pair  with given curve name
     */
    public static KeyPair generateKeys(){
        try {
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(CURVE_NAME);
            KeyPairGenerator generator = null;
            generator = KeyPairGenerator.getInstance("EC", "BC");
            SecureRandom rand = new SecureRandom();
            generator.initialize(ecSpec, rand);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }



    public static PrivateKey getPrivateKeyByFile(String privateKeyPath) throws Exception {

        File privKeyFile = new File(privateKeyPath);
        // read private key DER file
        DataInputStream dis = new DataInputStream(new FileInputStream(privKeyFile));
        byte[] privKeyBytes = new byte[(int)privKeyFile.length()];
        dis.read(privKeyBytes);
        dis.close();

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PrivateKey getPrivateKeyByString(String key) throws Exception {
        byte[] bytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }



    public static boolean verifyQrContent(String qrContent, String publickey) throws Exception {
        //!45!01PM64313100026102110020000000143000100021000001000001770003MCACDiGTH8kVQ/2aM9Q3G7ZqAg5+RGDVNAeNiuFaIBEeOQ==
        String signContent = qrContent.substring(4,64);
        String sign = qrContent.substring(64);
        log.info("signContent长度：" + signContent.length() + " 内容：" + signContent);
        log.info("publickey长度：" + publickey.length() + " 内容：" + publickey);
        log.info("sign长度：" + sign.length() + " 内容：" + sign);

        return verify(signContent, getPublicKey(publickey), Base64.getDecoder().decode(sign.getBytes(CHARSET_NAME)));
    }

    public static boolean verifyQrContent2(String qrContent, String signPath, String publickey) throws Exception {
        //!45!01PM64313100026102110020000000143000100021000001000001770003MCACDiGTH8kVQ/2aM9Q3G7ZqAg5+RGDVNAeNiuFaIBEeOQ==
        String signContent = qrContent.substring(4,64);
//        String sign = qrContent.substring(64);
        byte[] signBytes = cc.mrbird.febs.common.utils.FileUtil.readBytes(signPath);
        String sign = Base64.getEncoder().encodeToString(signBytes);
        log.info("signContent2长度：" + signContent.length() + " 内容：" + signContent);
        log.info("publickey2长度：" + publickey.length() + " 内容：" + publickey);
        log.info("sign2长度：" + sign.length() + " 内容：" + sign);
        log.info("signBytes2长度：" + signBytes.length);


        return verify(signContent, getPublicKey(publickey), signBytes);
    }

    public static String getBase64Sign(String signFile){
        return Base64.getEncoder().encodeToString(FileUtil.readBytes(signFile));
    }

   /* public String publicKeyToPEM(PublicKey pk){
        StringWriter sw = new StringWriter();
        PemWriter writer = new PemWriter(sw);
        try {
            writer.writeObject(new PemObject("PUBLIC KEY", pk.getEncoded()));
            writer.close();
        } catch (IOException e) {
            log.error("error writre public key  in PEM format ", e);
            throw new RuntimeException(e);
        }

        return sw.toString();
    }*/



   public static void main(String[] args) throws Exception {

       String content = "01PM64313100026102110020000000143000100021000001000001770003";
       String publicKey = "MEYwEAYHKoZIzj0CAQYFK4EEAB8DMgAE5sbBDnUDVmLQcykGcBAscj9CJ8f5OX2D\n" +
               "+SA6o+DE9XuBwCSeh+CvL60MY29BsLZi";
       String signContent = "MDQCF0oquAsNJAKEn/FktmeMdkOl/MJA1+U5AhkAilFWyWLC6uQqZSPF1E1uM+7OcB35/OYT";

       boolean verify_lai = verify(content, getPublicKey(publicKey), Base64.getDecoder().decode(signContent.getBytes(CHARSET_NAME)));
       log.info("lai 验证结果：" + verify_lai);

       publicKey = "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAE/Bvv7nPayhAnzSKScgHq/67xVDkI\n" +
               "21ORBKEEDDqAn+Hni3HyqFaQSl+iXrpkKMVL";
       signContent = "MDQCGEe5m94w2jvOdWJIk48Pmbq4a1FGbqv/0wIYIf5wS57QqriXu1bUgVdWCGoavsLHaSZ3";

       boolean verify_liu = verify(content, getPublicKey(publicKey), Base64.getDecoder().decode(signContent.getBytes(CHARSET_NAME)));
       log.info("liu 验证结果：" + verify_liu);

//       test1();
       checkLiu();
       /* String signFile = "C:\\Users\\Administrator\\Desktop\\test\\act.bin";
       String base64Sign = getBase64Sign(signFile);
       log.info("base64Sign.len = " + base64Sign.length() + " base64Sign = " + base64Sign);*/

   }

    private static void checkLiu() throws Exception {
        String publicKey = "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAE/Bvv7nPayhAnzSKScgHq/67xVDkI\n" +
                "21ORBKEEDDqAn+Hni3HyqFaQSl+iXrpkKMVL";

        String content = "01PM64313100026102110020000000143000100021000001000001770003";
        String qrContent = "!43!"+content+"MDUCGBFdYY8vKdAhG/o5VltXjn3cGaPf2x3l0AIZANYBLA009MowQdqTf59CUsYX4hCI7OJpxw==";


        boolean res1 = verifyQrContent(qrContent, publicKey);
        //boolean res2 =  verifyQrContent2(qrContent,"C:\\Users\\Administrator\\Desktop\\ecdsa\\act.bin",publicKey);
        log.info("验证小刘签名结果1：" + res1);
        //log.info("验证小刘签名结果2：" + res2);

        //java生成的公钥私钥
        /*publicKey = "MEYwEAYHKoZIzj0CAQYFK4EEAB8DMgAE56SouLrKgtOAz4bZfVoKQwsrB5+8ZF8ZEls//SNpFZ1V\n" +
                "8Y1LiFY/i2lBwCqqFQTS";
        privateKey = "MHUCAQAwEAYHKoZIzj0CAQYFK4EEAB8EXjBcAgEBBBiQBVNavlo00JZNjVWm3qgZKFxzVlUTf9Cg\n" +
                "BwYFK4EEAB+hNAMyAATnpKi4usqC04DPhtl9WgpDCysHn7xkXxkSWz/9I2kVnVXxjUuIVj+LaUHA\n" +
                "KqoVBNI=";*/

        //小刘测试的公钥私钥
//        publicKey = "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAE/Bvv7nPayhAnzSKScgHq/67xVDkI\n" +
//                "21ORBKEEDDqAn+Hni3HyqFaQSl+iXrpkKMVL";
//
//        String privateKeyPath = "C:\\Users\\Administrator\\Desktop\\ecdsa\\key-pkcs8.der";
//        String sign = signatureForData(content, getPrivateKeyByFile(privateKeyPath));
//        byte[] signDecodeBytes = Base64.getDecoder().decode(sign.getBytes(CHARSET_NAME));
//        boolean verifyRes = verify(content, getPublicKey(publicKey), signDecodeBytes);
//        log.info("用小刘公钥私钥签名验证：" + verifyRes);
//        FileUtil.writeBytes(signDecodeBytes,"C:\\Users\\Administrator\\Desktop\\ecdsa\\lai");


//        test2(content);

    }

    private static void test2(String content) throws Exception {
        log.info("");
        log.info("开始用java代码签名");
        KeyPair keyPair = generateKeys();
        PublicKey publicKey2 = keyPair.getPublic();
        PrivateKey privateKey2 = keyPair.getPrivate();
        String sign = signatureForData(content, privateKey2);
        byte[] signDecodeBytes = Base64.getDecoder().decode(sign.getBytes(CHARSET_NAME));
        boolean verifyRes = verify(content, publicKey2, signDecodeBytes);

        String publicKeyStr = new BASE64Encoder().encode(publicKey2.getEncoded());
        String privateKeyStr = new BASE64Encoder().encode(privateKey2.getEncoded());
        log.info("publicKey长度 = " + publicKeyStr.length() + " publicKey = " + publicKeyStr);
        log.info("privateKey长度 = " + privateKeyStr.length() + " privateKey = " + privateKeyStr);

        log.info("content.length = " + content.length() + " content = " + content);
        log.info("sign length  = " + sign.length() + "  sign = " + sign);
        log.info("sign decode bytes size = " + signDecodeBytes.length);
        log.info("verifyRes = " + verifyRes);
    }

    private static void test1() throws Exception {
        KeyPair keyPair = generateKeys();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String oldStr = "!45!01PM64313100026102110020000000143000100021000001000001770003";

        String content =    "01PM64313100026102110020000000143000100021000001000001770003";
        String test = "!45!  01PM64313100026102110020000000143000100021000001000001770003  MCACDiGTH8kVQ/2aM9Q3G7ZqAg5+RGDVNAeNiuFaIBEeOQ==";

        String sign = signatureForData(content, privateKey);
        byte[] signDecodeBytes = Base64.getDecoder().decode(sign.getBytes(CHARSET_NAME));
        boolean verifyRes = verify(content, publicKey, signDecodeBytes);

        log.info("content.length = " + content.length() + " content = " + content);
        log.info("sign length  = " + sign.length() + "  sign = " + sign);
        log.info("sign decode bytes size = " + signDecodeBytes.length);
        log.info("verifyRes = " + verifyRes);


        String publicKeyStr = new BASE64Encoder().encode(publicKey.getEncoded());
        boolean testVerify = verify(content, getPublicKey(publicKeyStr), Base64.getDecoder().decode(sign.getBytes(CHARSET_NAME)));
        log.info("publicKeyStr = " + publicKeyStr);
        log.info("testVerify result = " + testVerify);

        String qrContent =    "01PM64313100026102110020000000143000100021000001000001770003";
        String qrPublicKey = "MDIwEAYHKoZIzj0CAQYFK4EEAAYDHgAEyPdguaYv3FFD0AnEokKo/nfkkWmkoEy1\nOVnACw==";
        String qrSign = "MCACDiGTH8kVQ/2aM9Q3G7ZqAg5+RGDVNAeNiuFaIBEeOQ==";
        boolean qrVerify = verify(qrContent, getPublicKey(qrPublicKey), Base64.getDecoder().decode(qrSign.getBytes(CHARSET_NAME)));
        log.info("machine sign verity result = " + qrVerify);
    }


}
