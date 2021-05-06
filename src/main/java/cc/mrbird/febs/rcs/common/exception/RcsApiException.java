package cc.mrbird.febs.rcs.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;

/**
 * 俄罗斯访问我们接口时发生的异常
 */
@Slf4j
public class RcsApiException extends RuntimeException   {

    private static final long serialVersionUID = -5353798060276232903L;

    public RcsApiException(String message) {
        super(message);
        log.error("RcsApiException error = " + message);
    }
}
