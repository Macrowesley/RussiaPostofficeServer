package cc.mrbird.febs.audit.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class NoPassAuditDTO {
    @NotNull
    @Min(1)
    Long auditId;

    String checkRemark;

}
