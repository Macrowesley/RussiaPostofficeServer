package cc.mrbird.febs.rcs.common.context.order.process.enums;

/**
 * 订单的各种状态
 */
public enum OrderProcessTypeEnum {
    ;
    private int code;
    private String type;

    OrderProcessTypeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }
}
