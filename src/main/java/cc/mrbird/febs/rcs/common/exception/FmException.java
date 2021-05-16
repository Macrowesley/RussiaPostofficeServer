package cc.mrbird.febs.rcs.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FmException extends RuntimeException {

    private static final long serialVersionUID = 3094329149399295119L;
    private int code = -1;

    public FmException(String message) {
        super(message);
        log.error("FmException error = " + message);
    }

    public FmException(int code, String message ) {
        super(message);
        this.code = code;
        log.error("FmException error = " + message);
    }

    public int getCode() {
        return code;
    }
}
