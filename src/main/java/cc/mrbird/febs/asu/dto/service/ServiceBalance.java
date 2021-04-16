package cc.mrbird.febs.asu.dto.service;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBalance {
    /**
     * 合约编号
     * format: uuid
     */
    @NotBlank
    String contractId;

    /**
     * 整数合同ID，保留用于兼容性
     */
    @Deprecated
    Integer contractNum;

    /**
     * 当前可用资金（包括持有）
     */
    @NonNull
    Double current;

    /**
     * 当前余额（仅事实）
     */
    @NonNull
    Double consolidate;

    /**
     * format: uuid
     */
    @NotBlank
    String operationId;

    /**
     * format: 'date-time'
     */
    String timestamp;
}
