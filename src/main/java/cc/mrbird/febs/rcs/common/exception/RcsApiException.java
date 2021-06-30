package cc.mrbird.febs.rcs.common.exception;

import cc.mrbird.febs.rcs.common.enums.RcsApiErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;

/**
 * 俄罗斯访问我们接口时发生的异常
 */
@Slf4j
public class RcsApiException extends RuntimeException   {
    private int code = 1000;
    private static final long serialVersionUID = -5353798060276232903L;

    public RcsApiException(RcsApiErrorEnum errorEnum) {
        super(errorEnum.getMsg());
        this.code = errorEnum.getCode();
        log.error("RcsApiException error = " + errorEnum.getMsg());
    }


    public int getCode() {
        return code;
    }
}
