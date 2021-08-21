package cc.mrbird.febs.rcs.common.enums;


public enum FMStatusEnum {
    ADD_MACHINE_INFO(0,"ADD","机器出厂的时候添加信息到服务器数据库 数据库有了基本的机器信息，加上这个信息就完整了 这是自己定义的状态，不需要发给俄罗斯"),
    ENABLED(1,"ENABLED","授权"),
    DEMO(2,"DEMO","演示"),
    BLOCKED(3,"BLOCKED","已封锁"),
    UNAUTHORIZED(4,"UNAUTHORIZED","取消授权"),
    LOST(5,"LOST","lost");

    private final int code;
    private final String status;
    private final String message;
    FMStatusEnum(int code, String status, String message){
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public static FMStatusEnum getByCode(int statusType) {
        for (FMStatusEnum item: FMStatusEnum.values()){
            if (item.getCode()== statusType){
                return item;
            }
        }
        return null;
    }

    public static FMStatusEnum getByStatus(String status) {
        for (FMStatusEnum item: FMStatusEnum.values()){
            if (item.getStatus().equals(status)){
                return item;
            }
        }
        return null;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}

