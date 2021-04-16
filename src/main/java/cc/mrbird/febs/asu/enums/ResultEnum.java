package cc.mrbird.febs.asu.enums;

public enum ResultEnum {

    SUCCESS(200, "成功"),

    OPERATION_ERROR(400, "操作错误"),

    INTERNAL_SERVICE_ERROR(500, "内部服务器出错"),

    UNKNOW_ERROR(-1, "状态码不对");


    private int code;
    private String message;

    ResultEnum(int code, String message) {
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

    public static ResultEnum getByCode(int code){
        for (ResultEnum value : ResultEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}

