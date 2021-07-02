package cc.mrbird.febs.rcs.dto.manager;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ManagerBalanceDTO {
    /**
     * 自己数据库中
     */
    String balanceId;
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

    String timestamp;
}
