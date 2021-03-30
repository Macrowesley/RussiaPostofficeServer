package cc.mrbird.febs.asu.entity.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 操作错误，如果可能，还额外返回余额
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OperationError {
    @NotNull
    int code;
    @NotBlank
    String message;
    ManagerBalance managerBalance;
}
