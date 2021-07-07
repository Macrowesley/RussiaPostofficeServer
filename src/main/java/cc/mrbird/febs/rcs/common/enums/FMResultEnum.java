package cc.mrbird.febs.rcs.common.enums;

public enum FMResultEnum {
    //公共异常
    SUCCESS(1,"成功"),
    Overtime(2,"请求太频繁，1分钟内请勿重复请求"),
    NotFinish(3,"没有闭环，请等待"),
    VersionError(4,"版本信息不对"),
    StatusNotValid(5,"机器状态无效"),
    VisitRussiaTimedOut(6,"访问俄罗斯超时"),
    RussiaServerRefused(7,"俄罗斯服务器拒绝"),
    MoneyTooBig(8,"订单金额超过了合同金额"),
    TaxVersionNeedUpdate(9,"税率版本需要更新，机器需要更新税率表，然后通知服务器"),
    DonotAgain(10,"已闭环，请勿操作"),
    StatusTypeError(11,"未闭环，但是要改的状态不对"),
    PrivateKeyNeedUpdate(12,"服务器更新了私钥，机器需要需要更新"),
    DeviceNotFind(13,"表头号或者FrankMachineId不存在"),
    contractCodeAbnormal(14,"俄罗斯返回的contractCode异常"),
    MachineLogined(15,"机器已经登录了"),

    //transaction的异常
    TransactionError(31,"TransactionError异常，需要继续执行transaction"),
    TransactionExist(32,"transaction已经存在，不能新建"),

    //privateKey异常
    PrivateKeyNotExist(41,"privatekey不存在"),

    DmmsgIsNotFinish(51,"批次记录为奇数，有没有完成的批次"),

    DefaultError(0,"其他异常导致的失败");

    int code;
    String msg;

    FMResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getSuccessCode() {
        return String.format("%02d", code);
    }

    public String getMsg() {
        return msg;
    }
}
