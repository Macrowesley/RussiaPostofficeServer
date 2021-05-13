package cc.mrbird.febs.rcs.common.enums;


public enum FMStatusEnum {
    ADD_MACHINE_INFO(0,"ADD","机器出厂的时候添加信息到服务器数据库 数据库有了基本的机器信息，加上这个信息就完整了 这是自己定义的状态，不需要发给俄罗斯"),
    UNKNOWN(1,"UNKNOWN", "未知"),
    REGISTERED(2,"REGISTERED","注册"),
    AUTHORIZED(3,"AUTHORIZED","授权"),
    OPERATING(4,"OPERATING","操作的"),
    PENDING_WITHDRAWN(5,"PENDING_WITHDRAWN","待提款"),
    TEMPORARILY_WITHDRAWN(6,"TEMPORARILY_WITHDRAWN","暂时撤回"),
    PERMANENTLY_WITHDRAWN(7,"PERMANENTLY_WITHDRAWN","永久提款"),
    IN_TRANSFER(8,"IN_TRANSFER","转让中"),
    MISSING(9,"MISSING","丢失的"),
    SCRAPPED(10,"SCRAPPED","报废"),
    MAINTENENCE(11,"MAINTENENCE","维护"),
    BLOCKED(12,"BLOCKED","已封锁"),
    AUTH_CANCELED(13,"AUTH_CANCELED","取消授权"),
    DEMO(14,"DEMO","演示");

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
/*    ENABLED("ENABLED"),
    DEMO("DEMO"),
    BLOCKED("BLOCKED"),
    UNAUTHORIZED("UNAUTHORIZED"),
    LOST("LOST");

    private final String status;
    FMStatusEnum(String status){
        this.status = status;
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
    }*/

    /*ENABLED(1,"ENABLED", "可用的"),
    DEMO(2,"DEMO","准备在演示模式下工作"),
    BLOCKED(3,"BLOCKED","封锁"),
    UNAUTHORIZED(4,"UNAUTHORIZED","无权限的"),
    LOST(5,"LOST","停止使用");

    private final int type;
    private final String status;
    private final String message;
    FMStatusEnum(int type, String status, String message){
        this.type = type;
        this.status = status;
        this.message = message;
    }

    public static FMStatusEnum getByType(int statusType) {
        for (FMStatusEnum item: FMStatusEnum.values()){
            if (item.getType()== statusType){
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

    public int getType() {
        return type;
    }*/
}

