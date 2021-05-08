package cc.mrbird.febs.rcs.common.enums;

public enum TaxUpdateEnum {

    UPDATE(1, "已经更新到了最新版本"),

    NOT_UPDATE(0, "未更新到最新版本");


    private int code;
    private String message;

    TaxUpdateEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static TaxUpdateEnum getByCode(int code){
        for (TaxUpdateEnum value : TaxUpdateEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}

