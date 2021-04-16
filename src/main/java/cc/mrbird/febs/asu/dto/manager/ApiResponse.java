package cc.mrbird.febs.asu.dto.manager;

import cc.mrbird.febs.asu.enums.ResultEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ApiResponse {
    int code;
    Object object;

    ApiResponse(){

    }

    public ApiResponse(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public ApiResponse code(int code){
        this.code = code;
        return this;
    }

    public ApiResponse data(Object object){
        this.object = object;
        return this;
    }

    public boolean isOK(){
        return this.code == ResultEnum.SUCCESS.getCode();
    }
}
