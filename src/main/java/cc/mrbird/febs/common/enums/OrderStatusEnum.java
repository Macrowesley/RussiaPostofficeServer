package cc.mrbird.febs.common.enums;

public enum OrderStatusEnum {
    createOrder("0","创建订单"),
    beginApply("1","发起申请"),
    auditIng("2","审核中"),
    auditPass("3","审核通过"),
    auditNotPass("4","驳回"),
    machineGetData("5","机器获取数据包"),
    machineInjectionSuccess("6","机器注资成功"),
    orderRepeal("7","撤销"),
    orderFreeze("8","冻结"),
    orderCloseApplyIng("9","闭环申请中"),
    orderCloseApplyNotPass("10","闭环申请审核失败");//（闭环申请成功就是状态3）

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

    public static OrderStatusEnum getByStatus(int status) throws Exception {
        for (OrderStatusEnum item: OrderStatusEnum.values()){
            if (item.getStatus().equals(String.valueOf(status))){
                return item;
            }
        }
        throw new Exception("查无此订单类型");
    }

    public static void main(String[] args) throws Exception {
        int status = 1;
        System.out.println(OrderStatusEnum.getByStatus(status));
    }
}
