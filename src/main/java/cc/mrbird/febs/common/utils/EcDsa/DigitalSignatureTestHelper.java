package cc.mrbird.febs.common.utils.EcDsa;

import cc.mrbird.febs.common.utils.BaseTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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

       checkQrSign();

   }

    private static void checkQrSign() throws Exception {
        String content = "01PM64313100026102110020000000143000100021000001000001770003";
        String signBase64 = "MDQCGQDxfL9Xk+ax6eJ3/gqpu1AYFLUXxUh3irYCF23AkiPnagTNBoZOvNUndxFh8S2k7f88";
        String publicKey = "MEYwEAYHKoZIzj0CAQYFK4EEAB8DMgAEzdz/kuTttMaBTfAx9l4rSPi+k1H8jaNH\n" +
                "dPvHxi7zfBYa55wyLqThGccA894fo8qP";

        boolean res =  verify(content, getPublicKey(publicKey), signBase64);
        log.info("linux 验证结果：" + res);
    }
}
