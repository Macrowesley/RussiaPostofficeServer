package cc.mrbird.febs.common.entity;

public enum FMResultEnum {
    SUCCESS(1,"成功"),
    DefaultError(0,"其他异常导致的失败"),
    Overtime(2,"请求太频繁，1分钟内请勿重复请求"),
    TransactionError(3,"TransactionError异常，需要继续执行transaction"),
    NotFinish(4,"没有闭环，请等待"),
    VersionError(5,"版本信息不对");

    int code;
    String msg;

    FMResultEnum(int code, String msg) {
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
