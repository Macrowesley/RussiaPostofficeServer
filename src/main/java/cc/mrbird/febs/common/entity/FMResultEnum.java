package cc.mrbird.febs.common.entity;

public enum FMResultEnum {
    SUCCESS(1),
    FAIL(0);
    int code;

    FMResultEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
