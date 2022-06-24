package cc.mrbird.febs.rcs.dto.machine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdImageRes {
    //服务器图片id，必须要有
    String imageId;
    // 0 失败 1 成功
    String res;
}
