package cc.mrbird.febs.rcs.common.enums;


public enum  FlowEnum {
    FlowIng(0,"未闭环"),
    FlowEndSuccess(1,"闭环结束：成功"),
    FlowEndFail(-1,"闭环结束：失败");
    int code;
    String msg;

    FlowEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
