package cc.mrbird.febs.common.enums;

import cc.mrbird.febs.common.i18n.MessageUtils;

public enum OrderBtnEnum {
    //修改
    //提交注资审核
    //提交闭环
    //注销
    //冻结

    editBtn(1,"editBtn",MessageUtils.getMessage("order.btnEnum.editBtn")),
    submitInjectionBtn(2,"submitInjectionBtn",MessageUtils.getMessage("order.btnEnum.submitInjectionBtn")),
    submitCloseBtn(3,"submitCloseBtn",MessageUtils.getMessage("order.btnEnum.submitCloseBtn")),
    auditDetail(7,"auditDetail",MessageUtils.getMessage("order.btnEnum.auditDetail")),
    cancelBtn(4,"repealBtn",MessageUtils.getMessage("order.btnEnum.cancelBtn")),
    freezeBtn(5,"freezeBtn",MessageUtils.getMessage("order.btnEnum.freezeBtn")),
    unfreezeBtn(6,"unfreezeBtn",MessageUtils.getMessage("order.btnEnum.unfreezeBtn"));


    private int type;
    private String event;
    private String title;

    OrderBtnEnum(int type, String event,  String title) {
        this.type = type;
        this.event = event;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static OrderBtnEnum getByType(int type) throws Exception {
        for (OrderBtnEnum item: OrderBtnEnum.values()){
            if (item.getType() == type){
                return item;
            }
        }
        throw new Exception(MessageUtils.getMessage("order.btnEnum.noType"));
    }

    public static void main(String[] args) throws Exception {
        int type = 1;
        System.out.println(OrderBtnEnum.getByType(type));
    }

}
