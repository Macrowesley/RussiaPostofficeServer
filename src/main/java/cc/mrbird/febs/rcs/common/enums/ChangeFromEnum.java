package cc.mrbird.febs.rcs.common.enums;

public enum ChangeFromEnum {
    Russia(1,"俄罗斯"),
    Machine(0,"机器");
    int code;
    String msg;

    ChangeFromEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ChangeFromEnum getByCode(int code){
        for (ChangeFromEnum value : ChangeFromEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}
