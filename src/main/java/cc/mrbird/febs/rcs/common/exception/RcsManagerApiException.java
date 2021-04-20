package cc.mrbird.febs.rcs.common.exception;

/**
 * 我们调用俄罗斯接口时发生的异常
 */
public class RcsManagerApiException extends RuntimeException   {


    private static final long serialVersionUID = 9185532900454313846L;

    public RcsManagerApiException(String message) {
        super(message);
    }
}
