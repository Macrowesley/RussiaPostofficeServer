package cc.mrbird.febs.rcs.common.exception;

import cc.mrbird.febs.rcs.dto.manager.ManagerBalanceDTO;

/**
 * 我们调用俄罗斯接口时 关于金额的异常
 */
public class RcsManagerBalanceException extends RuntimeException   {

    ManagerBalanceDTO managerBalanceDTO;
    private static final long serialVersionUID = 7132725924437855121L;

    public RcsManagerBalanceException(String message, ManagerBalanceDTO managerBalanceDTO) {
        super(message);
        this.managerBalanceDTO = managerBalanceDTO;
    }

    public ManagerBalanceDTO getManagerBalanceDTO() {
        return managerBalanceDTO;
    }
}
