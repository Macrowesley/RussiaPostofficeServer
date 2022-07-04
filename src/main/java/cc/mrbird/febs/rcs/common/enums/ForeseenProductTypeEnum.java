package cc.mrbird.febs.rcs.common.enums;

/**
 * 产品类型
 */
public enum ForeseenProductTypeEnum {
    /**
     * 机器订单，默认为0，地址栏信息基本是空的，机器发不了地址信息
     */
    MACHINE_ADDRESS(0,"机器订单，默认为0，地址栏信息基本是空的，机器发不了地址信息"),
    /**
     * 文本地址
     */
    ADDRESS_CONTENT(1,"文本地址内容"),
    /**
     * 广告图片地址id
     */
    AD_iMAGE_ID(2,"广告图片地址id");
    private final int code;
    private final String msg;

    ForeseenProductTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public static ForeseenProductTypeEnum getByCode(int code){
        for (ForeseenProductTypeEnum value : ForeseenProductTypeEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}
