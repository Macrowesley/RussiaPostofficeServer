package cc.mrbird.febs.common.entity;


public enum LimitType {
    /**
     * 传统类型
     */
    CUSTOMER,
    /**
     *  根据 IP地址限制
     */
    IP,
    /**
     * 默认使用方法
     */
    DEFAULT
}
