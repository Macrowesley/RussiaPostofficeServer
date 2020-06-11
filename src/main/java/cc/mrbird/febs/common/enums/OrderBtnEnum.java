package cc.mrbird.febs.common.enums;

public enum OrderBtnEnum {
    //修改
    //提交注资审核
    //提交闭环
    //注销
    //冻结

    editBtn(1,"editBtn","修改"),
    submitInjectionBtn(2,"submitInjectionBtn","申请注资"),
    submitCloseBtn(3,"submitCloseBtn","申请闭环"),
    auditDetail(7,"auditDetail","审核详情"),
    cancelBtn(4,"repealBtn","注销"),
    freezeBtn(5,"freezeBtn","冻结"),
    unfreezeBtn(6,"unfreezeBtn","解冻");


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
        throw new Exception("查无此订单类型");
    }

    public static void main(String[] args) throws Exception {
        int type = 1;
        System.out.println(OrderBtnEnum.getByType(type));
    }

}
