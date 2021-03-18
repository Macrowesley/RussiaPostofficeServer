package cc.mrbird.febs.asu.entity.enums;

public enum  FMStatus {
    ENABLED("ENABLED", "可用的"),
    DEMO("DEMO","准备在演示模式下工作"),
    BLOCKED("BLOCKED","封锁"),
    UNAUTHORIZED("UNAUTHORIZED","无权限的"),
    LOST("LOST","停止使用");

    private final String status;
    private final String message;
    FMStatus(String status, String message){
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
