package cc.mrbird.febs.common.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 国际化处理类
 */
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
    public static String getMessage(String messageKey) {

        //根据应用部署的服务器系统来决定国际化
        String message = messageSource.getMessage(messageKey, null,  LocaleContextHolder.getLocale());

        //根据Request请求的语言来决定国际化
//        String message = messageSource.getMessage(messageKey, null, RequestContextUtils.getLocale(request));

        return message;
    }
}
