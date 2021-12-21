package cc.mrbird.febs.rcs.common.enums;

public enum WebSocketEnum {
    //公共异常
    ClickPrintRes(5,"点击打印结果"),
    CancelPrintRes(6,"取消打印结果"),
    ;


    int code;
    String msg;

    WebSocketEnum(int code, String msg) {
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
