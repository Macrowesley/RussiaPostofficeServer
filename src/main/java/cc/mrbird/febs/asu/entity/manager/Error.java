package cc.mrbird.febs.asu.entity.manager;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Error {
    int code;
    String message;
}
