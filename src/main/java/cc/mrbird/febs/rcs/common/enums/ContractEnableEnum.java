package cc.mrbird.febs.rcs.common.enums;

public enum ContractEnableEnum {
    ENABLE(1, "可用"),
    UNENABLE(0, "不可用");
    int code;
    String msg;

    ContractEnableEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ContractEnableEnum getByCode(int code) {
        for (ContractEnableEnum value : ContractEnableEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
