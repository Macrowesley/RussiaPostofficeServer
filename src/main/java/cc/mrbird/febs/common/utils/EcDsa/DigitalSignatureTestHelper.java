package cc.mrbird.febs.common.utils.EcDsa;

import cc.mrbird.febs.common.utils.BaseTypeUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
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

    public static boolean verify(String data, PublicKey publicKey, String sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        return signature.verify(Base64.getDecoder().decode(sign));
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

    public static String publicKeyToPem(PublicKey pk){
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
    }

    public static String publicKeyToPem(String pk){
        StringWriter sw = new StringWriter();
        PemWriter writer = new PemWriter(sw);
        try {
            writer.writeObject(new PemObject("PUBLIC KEY", getPublicKey(pk).getEncoded()));
            writer.close();
        } catch (IOException e) {
            log.error("error writre public key in PEM format ", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("error writre public key " + e.getMessage());
            throw new RuntimeException(e);
        }

        return sw.toString();
    }

    /**
     * 返回俄罗斯需要的格式
     * @param publicKey
     * @return
     */
    public static String russiaPublicKey(String publicKey){
        return JSON.toJSONString(publicKeyToPem(publicKey)).replace("\\r\\n", "&#xA;").replace("\"","");
    }

   public static void main(String[] args) throws Exception {

       checkQrSign();

   }

    private static void checkQrSign() throws Exception {
        String content = "01PM64313100023112110020000000158000500020110001000001770016";
        String signBase64 = "MDQCGF5n9xyYqzIF/8m7JDQVqyJhUtzbygcxjgIYbab/19Qy6Tk5Ow/iFwEvzQsWxkiUTzf5";
        String publicKey = "MEYwEAYHKoZIzj0CAQYFK4EEAB8DMgAEyhO+hgBuVFxz5gpCdGASpykzzGCDFVEB\n" +
                "Nwnz8NjCjwrs++Om8vkDYS5iiWdMiuE3";

        boolean res =  verify(content, getPublicKey(publicKey), signBase64);
        log.info("res：" + res);
        log.info(publicKeyToPem("MEYwEAYHKoZIzj0CAQYFK4EEAB8DMgAEv1WUnaPQgaU8KOQ+hWzVcjQAbnGlNr65\r\noCPNMAAGOJZQ+HQBEZHosknL9r9FEjtl"));
        log.info(russiaPublicKey("MEYwEAYHKoZIzj0CAQYFK4EEAB8DMgAEv1WUnaPQgaU8KOQ+hWzVcjQAbnGlNr65\r\noCPNMAAGOJZQ+HQBEZHosknL9r9FEjtl"));
    }
}
