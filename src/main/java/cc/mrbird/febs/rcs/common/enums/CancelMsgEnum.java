package cc.mrbird.febs.rcs.common.enums;

public enum CancelMsgEnum {
    Default(1,"未知原因");
    int code;
    String msg;

    CancelMsgEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static CancelMsgEnum getByCode(int code){
        for (CancelMsgEnum value : CancelMsgEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}
