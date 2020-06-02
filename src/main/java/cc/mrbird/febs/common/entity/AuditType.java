package cc.mrbird.febs.common.entity;

/**
 * 审核类型 1 请求注资  2 请求闭环
 */
public interface AuditType {
    /**
     * 1 请求注资
     */
    String injection = "1";
    /**
     * 2 请求闭环
     */
    String closedCycle = "2";

}
