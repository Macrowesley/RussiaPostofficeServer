package cc.mrbird.febs.common.exception;

/**
 * 限流异常 view
 *
 *
 */
public class LimitAccessViewException extends RuntimeException {

    private static final long serialVersionUID = -3608667856397125671L;

    public LimitAccessViewException(String message) {
        super(message);
    }
}