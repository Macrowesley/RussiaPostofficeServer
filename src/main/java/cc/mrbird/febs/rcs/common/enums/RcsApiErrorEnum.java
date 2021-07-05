package cc.mrbird.febs.rcs.common.enums;

public enum RcsApiErrorEnum {
    //公共异常
    WaitPrintJobFinish(1001,"Frank job is not finish, please wait"),
    WaitPublicKeyUpdateFinish(1002,"update public key is not finish, please wait"),
    WaitStatusChangeFinish(1003,"The last status change was not completed. Please wait"),
    UnknownFMId(1004,"Unknown FM Id "),
    ChangeStatusError(1005,"internal server error"),
    SaveBalanceError(1006,"internal server error"),
    SaveContractDtoError(1007,"internal server error"),
    SavePostOfficeDTOError(1008,"internal server error"),
    SavePublicKeyError(1009,"internal server error"),
    SaveTaxVersionError(1010,"internal server error")
    ;

    int code;
    String msg;

    RcsApiErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getSuccessCode() {
        return String.format("%02d", code);
    }

    public String getMsg() {
        return msg;
    }
}