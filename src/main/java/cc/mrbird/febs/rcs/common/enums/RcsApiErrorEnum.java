package cc.mrbird.febs.rcs.common.enums;

public enum RcsApiErrorEnum {
    //公共异常
    WaitPrintJobFinish(1001,"Frank job is not finish, please wait"),
    WaitPublicKeyUpdateFinish(1002,"update public key is not finish, please wait"),
    WaitStatusChangeFinish(1003,"The last status change was not completed. Please wait"),
    UnknownFMId(9912,"FM not found "),
    ChangeStatusError(1005,"internal server error"),
    SaveBalanceError(1006,"internal server error"),
    SaveContractDtoError(1007,"internal server error"),

    SavePostOfficeDTOError(1008,"internal server error"),
    SavePublicKeyError(1009,"internal server error"),
    SaveTaxVersionError(1010,"internal server error"),
    TaxVersionExist(1101,"tax version is exist"),
    ContractExist(1102,"contract is exist"),
    ContractNotExist(9913,"Contract not found"),
    InvalidTimezoneValue(1104,"Invalid timezone value"),
    InvalidTarrifZoneValue(1105,"Invalid tarrifZone value"),
    CurrentOrConsolidateIsNull(1105,"Current or Consolidate is null"),
    ContractNotSame(1106,"value in path not equal in json"),
    ModitifyDateIsOld(1007,"Modified date is old"),
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
