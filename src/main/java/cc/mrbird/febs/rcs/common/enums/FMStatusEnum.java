package cc.mrbird.febs.rcs.common.enums;

/*
1 ENABLED
2 DEMO
3 BLOCKED
4 UNAUTHORIZED
5 LOST
 */
public enum FMStatusEnum {
    ENABLED("ENABLED"),
    DEMO("DEMO"),
    BLOCKED("BLOCKED"),
    UNAUTHORIZED("UNAUTHORIZED"),
    LOST("LOST");

    private final String status;
    FMStatusEnum(String status){
        this.status = status;
    }

    public static FMStatusEnum getStatus(String status) {
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

    public static FMStatusEnum getStatusByType(int statusType) {
        for (FMStatusEnum item: FMStatusEnum.values()){
            if (item.getType()== statusType){
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

