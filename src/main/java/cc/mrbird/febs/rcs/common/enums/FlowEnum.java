package cc.mrbird.febs.rcs.common.enums;


import cc.mrbird.febs.common.i18n.MessageUtils;

public enum FlowEnum {
    FlowIng(0, MessageUtils.getMessage("printJob.noClosedLoop")),
    FlowEnd(1,MessageUtils.getMessage("printJob.closedFinished"));
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
