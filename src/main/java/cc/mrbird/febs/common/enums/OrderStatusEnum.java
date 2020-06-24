package cc.mrbird.febs.common.enums;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import org.apache.commons.lang3.StringUtils;

public enum OrderStatusEnum {
    createOrder("1",MessageUtils.getMessage("order.createOrder")),
    auditIng("2",MessageUtils.getMessage("order.auditIng")),
    auditPass("3",MessageUtils.getMessage("order.auditPass")),
    auditNotPass("4",MessageUtils.getMessage("order.auditNotPass")),
    machineGetData("5",MessageUtils.getMessage("order.machineGetData")),
    machineInjectionSuccess("6",MessageUtils.getMessage("order.machineInjectionSuccess")),//机器注资成功
    machineInjectionFail("7",MessageUtils.getMessage("order.machineInjectionFail")),
    orderCloseApplyIng("10",MessageUtils.getMessage("order.orderCloseApplyIng")),
    orderCloseApplyNotPass("11",MessageUtils.getMessage("order.orderCloseApplyNotPass")),//（闭环申请成功就是状态6）
    orderRepeal("8",MessageUtils.getMessage("order.orderRepeal")),
    orderFreeze("9",MessageUtils.getMessage("order.orderFreeze"));

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

    public static OrderStatusEnum getByStatus(String status)  {
        for (OrderStatusEnum item: OrderStatusEnum.values()){
            if (item.getStatus().equals(status)){
                return item;
            }
        }
        try {
            throw new Exception(MessageUtils.getMessage("order.btnEnum.noType"));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        int status = 1;
        System.out.println(OrderStatusEnum.getByStatus("1"));

        OrderStatusEnum statusEnum = OrderStatusEnum.getByStatus("8");
        System.out.println(statusEnum == OrderStatusEnum.machineInjectionSuccess);
        System.out.println(statusEnum == OrderStatusEnum.orderRepeal);
        if (!(statusEnum == OrderStatusEnum.machineInjectionSuccess || statusEnum == OrderStatusEnum.orderRepeal)) {
//            throw new FebsException("订单没有闭环/撤销，无法操作");
            System.out.println("111");
        }else{
            System.out.println("333");
        }
    }
}
