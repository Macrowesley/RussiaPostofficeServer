package cc.mrbird.febs.rcs.common.kit;

import cc.mrbird.febs.common.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * publickey创建工具
 */
@Slf4j
public class PublicKeyGenerate {
    private String privateKey;
    private String publicKey;

    public PublicKeyGenerate() {
        try {
            //生成密钥对(公钥和私钥)
            Map<String, Object> keyMap =  RSAUtils.genKeyPair();
            //获取私钥
            privateKey = RSAUtils.getPrivateKey(keyMap);
            //获取公钥
            publicKey = RSAUtils.getPublicKey(keyMap);
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
