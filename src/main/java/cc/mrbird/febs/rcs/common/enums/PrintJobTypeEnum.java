package cc.mrbird.febs.rcs.common.enums;

/**
 * 订单创建类型
 * @author Administrator
 */
public enum PrintJobTypeEnum {
    Machine(1,"机器创建的订单"),
    Web(2,"PC页面中创建的订单");
    int code;
    String msg;

    PrintJobTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static PrintJobTypeEnum getByCode(int code) {
        for (PrintJobTypeEnum item: PrintJobTypeEnum.values()){
            if (item.getCode()== code){
                return item;
            }
        }
        return null;
    }
}
