package cc.mrbird.febs.rcs.dto.manager;

import cc.mrbird.febs.rcs.common.enums.ResultEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiRcsResponse {
    int code;
    String message;
}
