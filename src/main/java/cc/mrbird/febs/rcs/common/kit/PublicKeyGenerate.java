package cc.mrbird.febs.rcs.common.kit;

import cc.mrbird.febs.common.utils.EcDsa.EcDsaCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.security.KeyPair;

/**
 * publickey创建工具
 */
@Slf4j
public class PublicKeyGenerate {
    private String privateKey;
    private String publicKey;

    public PublicKeyGenerate() {
        try {
            /*//生成密钥对(公钥和私钥)
            Map<String, Object> keyMap =  RSAUtils.genKeyPair();
            //获取私钥
            privateKey = RSAUtils.getPrivateKey(keyMap);
            //获取公钥
            publicKey = RSAUtils.getPublicKey(keyMap);*/

            KeyPair keyPair = EcDsaCode.initKey();
            publicKey = Hex.encodeHexString(keyPair.getPublic().getEncoded());
            privateKey = Hex.encodeHexString(keyPair.getPrivate().getEncoded());
        } catch (Exception e) {
            log.error("公钥创建出问题：" + e.getMessage());
        }
    }


    public String getPublicKey(){
        return publicKey;
    }

    public String getPrivateKey(){
        return privateKey;
    }
}
