package cc.mrbird.febs.rcs.dto.machine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdImageInfo {
    //服务器图片id，必须要有
    String imageId;
    //文件名，带后缀
    String fileName;
    //下载地址
    String downLoadUrl;
}
