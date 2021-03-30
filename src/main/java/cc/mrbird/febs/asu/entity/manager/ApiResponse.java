package cc.mrbird.febs.asu.entity.manager;

import cc.mrbird.febs.asu.entity.enums.ResultEnum;
import lombok.Data;
import lombok.ToString;
import org.apache.poi.ss.formula.functions.T;

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
