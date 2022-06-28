package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.req.AdImageAddReq;
import cc.mrbird.febs.rcs.dto.machine.AdImageInfo;
import cc.mrbird.febs.rcs.entity.AdImage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

public interface IAdImageService extends IService<AdImage> {
    /**
     * 查询（分页）
     */
    IPage<AdImage> list(QueryRequest request, AdImage bean);

    /**
     * 获取发给机器的图片数组
     * @param frankMachineId
     * @return
     */
    AdImageInfo[] getAdImageInfoArr(String frankMachineId);

    /**
     * 上传图片
     * @param mf
     */
    String upload(MultipartFile mf);

    /**
     * 添加图片
     * @param adImageRes
     */
    void addImage(AdImageAddReq adImageAddReq);

    /**
     * 删除图片
     * @param adImageId
     */
    void delImage(Integer adImageId);

    /**
     * 同步图片给机器
     * @param frankMachineId
     */
    void syncImageList(String frankMachineId);

    int selectCout(String frankMachineId);
}
