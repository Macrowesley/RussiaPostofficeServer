package cc.mrbird.febs.rcs.common.enums;

/**
 * 订单创建类型
 * @author Administrator
 */
public enum OrderTypeEnum {
    Machine(1,"机器创建的订单"),
    Web(2,"页面中创建的订单");
    int code;
    String msg;

    OrderTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static OrderTypeEnum getByCode(int code) {
        for (OrderTypeEnum item: OrderTypeEnum.values()){
            if (item.getCode()== code){
                return item;
            }
        }
        return null;
    }
}
