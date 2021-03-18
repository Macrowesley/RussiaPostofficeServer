package cc.mrbird.febs.asu.entity.manager;

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
}
