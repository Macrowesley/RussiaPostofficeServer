package cc.mrbird.febs.common.enums;

public enum AuditBtnEnum {
    passBtn(1, "pass", "通过"),
    noPassBtn(2, "noPass", "驳回");

    private int type;
    private String event;
    private String title;

    AuditBtnEnum(int type, String event, String title) {
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

    public static AuditBtnEnum getByType(int type) throws Exception {
        for (AuditBtnEnum item : AuditBtnEnum.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        throw new Exception("查无此审核按钮类型");
    }
}
