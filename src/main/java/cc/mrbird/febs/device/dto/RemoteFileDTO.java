package cc.mrbird.febs.device.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RemoteFileDTO {
    @NotNull
    @Min(1)
    Long deviceId;

    @NotBlank
    String acnum;

    /**
     * 需要更新的文件在远程机器中的路径
     */
    String remoteFilePath;

    /**
     * 文件下载地址
     */
    String url;

    String md5;
}
