package cc.mrbird.febs.common.utils.EcDsa;

import cc.mrbird.febs.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
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

    public static boolean verifyQrContent(String qrContent) throws Exception {
        //!45!01PM64313100026102110020000000143000100021000001000001770003MCACDiGTH8kVQ/2aM9Q3G7ZqAg5+RGDVNAeNiuFaIBEeOQ==
        String signContent = qrContent.substring(4,64);
        String publickey = "MDIwEAYHKoZIzj0CAQYFK4EEAAYDHgAEyPdguaYv3FFD0AnEokKo/nfkkWmkoEy1\nOVnACw==";
        String sign = qrContent.substring(64);
        log.info("signContent长度：" + signContent.length() + " 内容：" + signContent);
        log.info("publickey长度：" + publickey.length() + " 内容：" + publickey);
        log.info("sign长度：" + sign.length() + " 内容：" + sign);

        return verify(signContent, getPublicKey(publickey), Base64.getDecoder().decode(sign.getBytes(CHARSET_NAME)));
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

       /*String qrContent = "!45!01PM64313100026102110020000000143000100021000001000001770003MCACDiGTH8kVQ/2aM9Q3G7ZqAg5+RGDVNAeNiuFaIBEeOQ==";
       log.info("二维码验证结果：" + verifyQrContent(qrContent));
       test1();*/
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
