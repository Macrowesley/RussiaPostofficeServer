package cc.mrbird.febs.rcs.common.context.order.status.enums;

/**
 * @author Administrator
 */
public enum RcsOrderStatusEnum {
    /**
     1 订单刚刚创建成功
     2 foreseen操作
                        俄罗斯同意
                        俄罗斯拒绝
                        网络异常
       transaction 操作：
                        给机器发送成功
                        给机器发送失败
                        俄罗斯同意
                        俄罗斯拒绝
                        网络异常
     */
    Begin(1,"begin", "订单刚刚创建，没有做其他操作"),

    ;

    int code;
    String status;
    String msg;

    RcsOrderStatusEnum(int code, String alias, String msg) {
        this.code = code;
        this.status = alias;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public static RcsOrderStatusEnum getByCode(int code){
        for (RcsOrderStatusEnum value : RcsOrderStatusEnum.values()) {
            if (value.getCode() == code){
                return value;
            }
        }
        return null;
    }

    public static RcsOrderStatusEnum getByStatus(String status){
        for (RcsOrderStatusEnum value : RcsOrderStatusEnum.values()) {
            if (value.getStatus() == status){
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RcsOrderStatusEnum{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

}
