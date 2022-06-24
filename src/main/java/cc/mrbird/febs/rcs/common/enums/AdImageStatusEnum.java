package cc.mrbird.febs.rcs.common.enums;

public enum AdImageStatusEnum {
    /**
     * FM下载成功
     */
    SUCCESS(1,"download success"),
    /**
     * FM下载失败
     */
    Fail(0,"download fail");
    private final int code;
    private final String status;

    AdImageStatusEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public static AdImageStatusEnum getByCode(int code){
        for (AdImageStatusEnum value : AdImageStatusEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }
}
