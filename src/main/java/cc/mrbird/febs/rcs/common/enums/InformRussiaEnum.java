package cc.mrbird.febs.rcs.common.enums;

/**
 * @author lai
 */
public enum InformRussiaEnum {

    YES(1, "已经通知了俄罗斯"),

    NO(0, "未通知俄罗斯");


    private int code;
    private String message;

    InformRussiaEnum(int code, String message) {
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

    public static InformRussiaEnum getByCode(int code){
        for (InformRussiaEnum value : InformRussiaEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}

