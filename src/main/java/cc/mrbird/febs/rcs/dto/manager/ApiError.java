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
public class ApiError {
    int code;
    String message;

    @JsonIgnore
    public boolean isOK(){
        return this.code == ResultEnum.SUCCESS.getCode() || this.code == ResultEnum.SUCCESS_204.getCode() || this.code == ResultEnum.SUCCESS_201.getCode();
    }
}
