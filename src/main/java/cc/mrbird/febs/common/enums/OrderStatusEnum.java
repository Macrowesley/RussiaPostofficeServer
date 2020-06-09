package cc.mrbird.febs.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum OrderStatusEnum {
    createOrder("1","创建订单"),
    auditIng("2","注资审核中"),
    auditPass("3","注资审核通过"),
    auditNotPass("4","注资审核失败"),
    machineGetData("5","机器获取数据包"),
    machineInjectionSuccess("6","闭环成功"),//机器注资成功
    machineInjectionFail("7","机器注资失败"),
    orderCloseApplyIng("10","闭环申请中"),
    orderCloseApplyNotPass("11","闭环申请审核失败"),//（闭环申请成功就是状态6）
    orderRepeal("8","注销"),
    orderFreeze("9","冻结");

    private String status;
    private String msg;

    OrderStatusEnum(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public static OrderStatusEnum getByStatus(String status) throws Exception {
        for (OrderStatusEnum item: OrderStatusEnum.values()){
            if (item.getStatus().equals(status)){
                return item;
            }
        }
        throw new Exception("查无此订单类型");
    }

    public static void main(String[] args) throws Exception {
        int status = 1;
        System.out.println(OrderStatusEnum.getByStatus("1"));
    }
}
