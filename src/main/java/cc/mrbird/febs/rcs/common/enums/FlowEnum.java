package cc.mrbird.febs.rcs.common.enums;


public enum FlowEnum {
    FlowIng(1,"未闭环"),
    FlowEnd(0,"闭环结束");
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

    public static FlowEnum getByCode(int code) {
        for (FlowEnum item: FlowEnum.values()){
            if (item.getCode()== code){
                return item;
            }
        }
        return null;
    }
}
