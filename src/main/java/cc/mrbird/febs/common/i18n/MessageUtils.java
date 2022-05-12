package cc.mrbird.febs.common.i18n;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 国际化处理类
 */
@Slf4j
@Component
public class MessageUtils {
    private static MessageSource messageSource;

    @Autowired
    private HttpServletRequest request;

    public MessageUtils(MessageSource messageSource) {
       this.messageSource = messageSource;
    }

    /**
     * 获取单个国际化翻译值
     */
    public String getMessage(String messageKey) {
        //根据Request请求的语言来决定国际化
        try {
            log.info("语言环境 " + request.getLocale().getLanguage().toString());
            String message = messageSource.getMessage(messageKey, null, RequestContextUtils.getLocale(request));
            return message;
        }catch (Exception e){
            return getStaticMessage(messageKey);
        }
    }

    public static String getStaticMessage(String messageKey) {
        //根据应用部署的服务器系统来决定国际化
        String message = messageSource.getMessage(messageKey, null,  LocaleContextHolder.getLocale());
        return message;
    }

}
