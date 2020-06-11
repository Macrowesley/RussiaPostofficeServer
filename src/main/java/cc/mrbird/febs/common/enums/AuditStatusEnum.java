package cc.mrbird.febs.common.enums;

public enum AuditStatusEnum {
    notBegin("0","未审核"),
    success("1","审核通过"),
    notPass("2","审核不通过"),
    orderFreezeing("3","冻结中"),
    orderRepeal("4","已注销");

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
        throw new Exception("查无此审核类型");
    }

    public static void main(String[] args) throws Exception {
        System.out.println(AuditStatusEnum.getByStatus("1"));
    }
}
