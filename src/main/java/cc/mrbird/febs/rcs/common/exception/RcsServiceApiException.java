package cc.mrbird.febs.rcs.common.exception;

import org.springframework.http.converter.HttpMessageConversionException;

/**
 * 俄罗斯访问我们接口时发生的异常
 */
public class RcsServiceApiException extends RuntimeException   {

    private static final long serialVersionUID = -5353798060276232903L;

    public RcsServiceApiException(String message) {
        super(message);
    }
}
