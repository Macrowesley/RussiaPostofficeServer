package cc.mrbird.febs.rcs.dto.machine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 发给机器的广告图片信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdInfoDTO {
    AdImageInfo[] adImageList;//广告列表
}
