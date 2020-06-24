package cc.mrbird.febs.common.enums;

import cc.mrbird.febs.common.i18n.MessageUtils;

public enum AuditStatusEnum {
    notBegin("0", MessageUtils.getMessage("audit.notBegin")),
    success("1",MessageUtils.getMessage("audit.success")),
    notPass("2",MessageUtils.getMessage("audit.notPass")),
    orderFreezeing("3",MessageUtils.getMessage("audit.orderFreezeing")),
    orderRepeal("4",MessageUtils.getMessage("audit.orderRepeal"));

    private String status;
    private String msg;

    AuditStatusEnum(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static AuditStatusEnum getByStatus(String status) throws Exception {
        for (AuditStatusEnum item: AuditStatusEnum.values()){
            if (item.getStatus().equals(status)){
                return item;
            }
        }
        throw new Exception(MessageUtils.getMessage("audit.status.noType"));
    }

    public static void main(String[] args) throws Exception {
        System.out.println(AuditStatusEnum.getByStatus("1"));
    }
}
