package cc.mrbird.febs.rcs.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FmException extends RuntimeException {

    private static final long serialVersionUID = 3094329149399295119L;

    public FmException(String message) {
        super(message);
        log.error("FmException error = " + message);
    }
}
