package cc.mrbird.febs.rcs.common.enums;

import cc.mrbird.febs.rcs.common.exception.FmException;

/**
 * auth请求的各种流程分析以及应对策略
 */
public enum FlowDetailEnum {
    DEFAULT(0, "最初始状态"),
    AuthEndSuccess(21, "闭环：服务器收到，发给俄罗斯，收到返回，已经发送publickey给俄罗斯，收到俄罗斯返回"),
    AuthEndFail(22, "闭环：服务器收到，发给俄罗斯，收到返回，但返回状态码不是200"),
    AuthError1(23, "未闭环：服务器收到，发给俄罗斯，未收到俄罗斯返回"),
    AuthError2(24, "未闭环：服务器收到，发给俄罗斯，收到返回，已经发送publickey给俄罗斯，未收到俄罗斯返回"),
    UnauthEndSuccess(31,"闭环：服务器收到，发给俄罗斯，俄罗斯返回OK"),
    UnAuthEndFail(32,"闭环：服务器收到，发给俄罗斯，俄罗斯返回 error "),
    UnAuthError(32,"未闭环：服务器收到，发给俄罗斯，未收到俄罗斯返回"),
    LostEndSuccess(41,""),
    LostEndFail(42,""),
    LostError(43,"错误1"),
    TaxError1(51,""),
    JobEndSuccess(61,""),
    JobEndFail(62,""),
    JobError1(63,""),
    BalanceEndSuccess(63,""),
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
        throw new FmException("code=" + code + ",没有对应的FlowDetailEnum");
    }

    public static FlowDetailEnum getByCode(int code, int min, int max) {
        if (code < min || code > max) {
            throw new FmException("不在范围内 code=" + code + ", min=" + min + " max = " + max);
        }
        for (FlowDetailEnum item : FlowDetailEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        throw new FmException("code=" + code + ",没有对应的FlowDetailEnum");
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
