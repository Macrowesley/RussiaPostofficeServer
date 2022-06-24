package cc.mrbird.febs.rcs.dto.machine;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 机器返回下载详情
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdInfoResDTO {
    //广告列表
    AdImageRes[] adImageList;
}