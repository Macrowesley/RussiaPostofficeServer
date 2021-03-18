package cc.mrbird.febs.asu.entity.manager;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
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
}
