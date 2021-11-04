package cc.mrbird.febs.rcs.common.enums;

import cc.mrbird.febs.rcs.common.exception.FmException;

/**
 * auth请求的各种流程分析以及应对策略
 */
public enum FlowDetailEnum {
    DEFAULT(0, "最初始状态"),
    StatusChangeEndSuccess(11,"闭环：状态修改成功"),
    StatusChangeEndFailUnKnow(13,"闭环：返回4XX或者5XX错误"),
    StatusChangeError4xx(12,"未闭环：未成功发送给俄罗斯"),
    StatusChangeBegin(14,"未闭环：状态改动中"),

    AuthEndSuccess(21, "闭环：服务器收到，发给俄罗斯，收到返回"),
    AuthEndFail(22, "闭环：服务器收到，发给俄罗斯，请求返回4XX或者5XX错误"),
    AuthErrorUnKnow(23, "未闭环：服务器收到，未成功发送给俄罗斯"),

    UnauthEndSuccess(31,"闭环：服务器收到，发给俄罗斯，俄罗斯返回OK"),
    UnAuthEndFail(32,"闭环：服务器收到，发给俄罗斯，请求返回4XX或者5XX错误 "),
    UnAuthErrorUnkonw(32,"未闭环：服务器收到，未成功发送给俄罗斯"),

    LostEndSuccess(41,"闭环：服务器收到，发给俄罗斯，俄罗斯返回OK"),
    LostEndFail(42,"闭环：服务器收到，发给俄罗斯，请求返回4XX或者5XX错误"),
    LostErrorUnknow(43,"未闭环：服务器收到，未成功发送给俄罗斯"),

    //打印任务：结束的各种状态
    JobEndSuccess(61,"闭环：Foreseen和Transaction 都成功了"),
    JobEndFailForeseensUnKnow(62,"闭环：Foreseen请求，未成功发送给俄罗斯"),
    JobEndFailForeseens4xx(63,"闭环：Foreseen请求返回4XX或者5XX错误"),
    JobEndFailForeseensCancelSuccess(64,"闭环：ForeseensCancel请求成功，闭环"),
    //打印任务：异常的各种状态
    JobErrorForeseensCancelUnKnow(65,"未闭环：ForeseensCancel请求，未成功发送给俄罗斯"),
    JobErrorForeseensCancel4xx(66,"未闭环：ForeseensCancel请求返回4XX或者5XX错误"),
    JobErrorTransactionUnKnow(67,"未闭环：Transaction请求，未成功发送给俄罗斯"),
    JobErrorTransaction4xx(68,"未闭环：Transaction请求返回4XX或者5XX错误"),
    //打印任务：进行中的状态
    JobingForeseensSuccess(69,"未闭环：Foreseen请求成功"),

    PublicKeyEndSuccess(81,"闭环：publickey流程结束"),
    PublicKeyingBegin(82,"未闭环：publickey创建/更新"),
    PublicKeyError4xx(83,"未闭环：publickey 请求返回4XX或者5XX错误"),
    PublicKeyErrorUnKnow(84,"未闭环：publickey请求，未成功发送给俄罗斯"),

    end(-1,"占位");
    int code;
    String msg;

    FlowDetailEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static FlowDetailEnum getByCode(int code) {
        for (FlowDetailEnum item : FlowDetailEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        throw new RuntimeException("code=" + code + ",没有对应的FlowDetailEnum");
    }

    public static FlowDetailEnum getByCode(int code, int min, int max) {
        if (code < min || code > max) {
            throw new RuntimeException("不在范围内 code=" + code + ", min=" + min + " max = " + max);
        }
        for (FlowDetailEnum item : FlowDetailEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        throw new RuntimeException("code=" + code + ",没有对应的FlowDetailEnum");
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
