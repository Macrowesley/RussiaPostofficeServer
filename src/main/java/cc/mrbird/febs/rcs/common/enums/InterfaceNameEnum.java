package cc.mrbird.febs.rcs.common.enums;

public enum InterfaceNameEnum {
    Auth(1,"auth"),
    UnAuth(2,"UnAuth"),
    CHANGE_STATUS(3,"change status"),
    TEST(0,"auth"),
    ;
    int code;
    String msg;

    InterfaceNameEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static InterfaceNameEnum getByCode(int code){
        for (InterfaceNameEnum value : InterfaceNameEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}
