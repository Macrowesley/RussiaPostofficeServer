package cc.mrbird.febs.rcs.dto.manager;

import cc.mrbird.febs.rcs.common.enums.ResultEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ApiRussiaResponse {
    int code;
    Object object;

    public ApiRussiaResponse(){

    }

    public ApiRussiaResponse(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    @JsonIgnore
    public boolean isOK(){
        return this.code == ResultEnum.SUCCESS.getCode() || this.code == ResultEnum.SUCCESS_204.getCode() || this.code == ResultEnum.SUCCESS_201.getCode();
    }
}
