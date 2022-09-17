package cc.mrbird.febs.common.license;

import cc.mrbird.febs.common.properties.FebsProperties;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * License校验类
 *
 * @author zifangsky
 * @date 2018/4/20
 * @since 1.0.0
 */
@Slf4j
@Component
public class LicenseVerifyUtils {
    private static Logger logger = LogManager.getLogger(LicenseVerifyUtils.class);

    @Autowired
    FebsProperties febsProperties;

    /**
     * 安装License证书
     *
     * @author zifangsky
     * @date 2018/4/20 16:26
     * @since 1.0.0
     */
    public synchronized boolean install() {

        String licensePath = febsProperties.getLicense().getLicensePath();
        String subject = febsProperties.getLicense().getSubject();
        String publicAlias = febsProperties.getLicense().getPublicAlias();
        String storePass = febsProperties.getLicense().getStorePass();
        storePass = "12345678A";//public_gdpt@2022
        String publicKeysStorePath = febsProperties.getLicense().getPublicKeysStorePath();

        if (StringUtils.isNotBlank(licensePath)) {
            log.info("++++++++ 开始安装证书 ++++++++");

            LicenseVerifyParam param = new LicenseVerifyParam();
            param.setSubject(subject);
            param.setPublicAlias(publicAlias);
            param.setStorePass(storePass);
            param.setLicensePath(licensePath);
            param.setPublicKeysStorePath(publicKeysStorePath);


            LicenseContent result = null;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //1. 安装证书
            try {
                LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
                licenseManager.uninstall();

                log.info("param = " + param.toString());
                result = licenseManager.install(new File(param.getLicensePath()));

                logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}", format.format(result.getNotBefore()), format.format(result.getNotAfter())));

                return true;
            } catch (Exception e) {
                logger.error("证书安装失败！", e);
                return false;
            }
        }else{
            log.error("证书文件" + licensePath + "不存在");
            return false;
        }
    }

    /**
     * 校验License证书
     *
     * @return boolean
     * @author zifangsky
     * @date 2018/4/20 16:26
     * @since 1.0.0
     */
    public boolean verify() {
        CustomLicenseManager licenseManager = (CustomLicenseManager) LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2. 校验证书
        logger.info("开始校验");
        try {
            LicenseContent licenseContent = licenseManager.verify();
            System.out.println("证书校验中：" + licenseContent.getSubject());
            System.out.println("证书校验中：" + licenseContent.getNotAfter());

            logger.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",
                    format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
            return true;
        } catch (Exception e) {
            logger.error("证书校验失败！", e);
            return false;
        }
    }

    /**
     * 初始化证书生成参数
     *
     * @param param License校验类需要的参数
     * @return de.schlichtherle.license.LicenseParam
     * @author zifangsky
     * @date 2018/4/20 10:56
     * @since 1.0.0
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerifyUtils.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerifyUtils.class
                , param.getPublicKeysStorePath()
                , param.getPublicAlias()
                , param.getStorePass()
                , null);

        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , publicStoreParam
                , cipherParam);
    }

}
