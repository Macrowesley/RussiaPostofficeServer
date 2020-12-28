package cc.mrbird.febs.system.dto;

import cc.mrbird.febs.common.annotation.IsMobile;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserUpdateDTO {

    private Long userId;
    private String username;
    private String realname;

    @IsMobile(message = "{mobile}")
    private String mobile;

    @Email(message = "{email}")
    private String email;

    private String roleId;

    private String deptIds;

    @NotBlank(message = "{required}")
    private String status;

    @NotBlank(message = "{required}")
    private String sex;

    @Size(max = 100, message = "{noMoreThan}")
    private String description;
}
