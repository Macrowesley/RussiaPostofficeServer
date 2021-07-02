package cc.mrbird.febs.rcs.dto.service;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBalanceDTO {
    /**
     * 合约编号
     * format: uuid
     */
    @NotBlank
    String contractCode;

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
    String modified;
}
