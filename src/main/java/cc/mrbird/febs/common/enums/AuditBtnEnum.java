package cc.mrbird.febs.common.enums;

import cc.mrbird.febs.common.i18n.MessageUtils;

public enum AuditBtnEnum {
    passBtn(1, "pass", MessageUtils.getMessage("audit.BtnEnum.passBtn")),
    noPassBtn(2, "noPass", MessageUtils.getMessage("audit.BtnEnum.noPassBtn"));

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
        throw new Exception(MessageUtils.getMessage("audit.BtnEnum.noType"));
    }
}
