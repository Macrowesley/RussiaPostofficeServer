package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.netty.protocol.ServiceToMachineProtocol;
import cc.mrbird.febs.rcs.req.AdImageAddReq;
import cc.mrbird.febs.rcs.common.enums.AdImageStatusEnum;
import cc.mrbird.febs.rcs.dto.machine.AdImageInfo;
import cc.mrbird.febs.rcs.entity.AdImage;
import cc.mrbird.febs.rcs.mapper.AdImageMapper;
import cc.mrbird.febs.rcs.service.IAdImageService;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AdImageServiceImpl extends ServiceImpl<AdImageMapper, AdImage> implements IAdImageService {
    @Autowired
    ServiceToMachineProtocol serviceToMachineProtocol;

    @Autowired
    IPrintJobService printJobService;

    @Value("${info.base-path}")
    private String basePath;

    @Value("${info.download-base-url}")
    private String downLoadBaseUrl;

    //存放idImage的主要目录
    private String parentDirName = "adImage";

    /**
     * 查询（分页）
     *
     * @param request
     * @param bean
     */
    @Override
    public IPage<AdImage> list(QueryRequest request, AdImage bean) {
        LambdaQueryWrapper<AdImage> queryWrapper = new LambdaQueryWrapper<>();
        if(bean == null){
            bean = new AdImage();
        }
        if (StringUtils.isNotBlank(bean.getFrankMachineId())){
            queryWrapper.eq(AdImage::getFrankMachineId,bean.getFrankMachineId());
        }

        Page<AdImage> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    /**
     * 获取发给机器的图片数组
     *
     * @param frankMachineId
     * @return
     */
    @Override
    public AdImageInfo[] getAdImageInfoArr(String frankMachineId) {
        //获取机器adimage列表
        LambdaQueryWrapper<AdImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdImage::getFrankMachineId, frankMachineId);
        List<AdImage> adImageList = this.baseMapper.selectList(wrapper);

        int size = adImageList.size();
        AdImageInfo[] adImageInfoArr = new AdImageInfo[size];
        for (int i = 0; i < size; i++) {
            AdImage bean = adImageList.get(i);
            AdImageInfo adImageInfo = new AdImageInfo();

            String url = bean.getImagePath();
            String[] split = url.split("/");
            String fileName = split[split.length -1];

            adImageInfo.setImageId(String.valueOf(bean.getId()));
            adImageInfo.setDownLoadUrl(url);
            adImageInfo.setFileName(fileName);

            adImageInfoArr[i] = adImageInfo;
        }

        return adImageInfoArr;
    }

    /**
     * 上传图片
     *
     * @param mf
     */
    @Override
    public String upload(MultipartFile mf) {
        String url = "";
        try {
            String curFileName = mf.getOriginalFilename();
            String type = mf.getContentType();
            String newName = String.valueOf(System.currentTimeMillis()) + curFileName.substring(curFileName.lastIndexOf("."));

            //判断类型
            String targetType = "image/jpeg";
            if (!targetType.equals(type)) {
                throw new FebsException("上传的文件类型不是" + targetType);
            }

            //判断图片大小
            /*long curSize = mf.getSize();
            log.info("图片大小={}",curSize);
            long targetSize = 1 *1024;
            if (curSize > targetSize){
                throw new FebsException("图片大小不能超过" + targetSize + "byte");
            }*/


            //判断长宽
            BufferedImage image = ImageIO.read(mf.getInputStream());
            if (image == null) {
                throw new FebsException("图片无法识别");
            }

            int curWidth = image.getWidth();
            int curHeight = image.getHeight();

            log.info("图片宽={}, 高={} 单位px", curWidth, curHeight);

            int targetMaxWidth = 300;
            int targetHeigth = 720;
            if (curWidth <= targetMaxWidth || curHeight != targetHeigth) {
                throw new FebsException("上传的文件尺寸不符合要求，必须是：宽不超过" + targetMaxWidth + "px, 高等于" + targetHeigth+ "px");
            }


            String dirName = DateUtil.format(new Date(), "yyyy-MM-dd");

            File file = FileUtil.file(FileUtil.mkdir(basePath + parentDirName + "\\" + dirName), newName);
//            mf.transferTo(file);
            //图片xy方向都压缩0.5
            Thumbnails.of(image).scale(0.5).toFile(file);

            url = downLoadBaseUrl + parentDirName + "/" + dirName + "/" + newName;

            log.info("basePath = " + basePath);
            log.info("type = " + type);
            log.info("curFileName = " + curFileName);
            log.info("url = " + url);

            return url;
        } catch (Exception e) {
            throw new FebsException("上传失败 " + e.getMessage());
        }

    }

    /**
     * 添加图片
     *
     * @param adImageRes
     */
    @Override
    @Transactional(rollbackFor = FebsException.class)
    public void addImage(AdImageAddReq adImageAddReq) {
        log.info("adImageAddReq = " + adImageAddReq.toString());
        String frankMachineId = adImageAddReq.getFrankMachineId();

        if (selectCout(frankMachineId) >= 9){
            throw new FebsException(frankMachineId+"绑定图片不能超过9个，请删除后重试");
        }

        //保存
        AdImage adImage = new AdImage();
        adImage.setFrankMachineId(frankMachineId);
        adImage.setImagePath(adImageAddReq.getUrl());
        adImage.setStatus(AdImageStatusEnum.Fail.getCode());
        adImage.setCreatedTime(new Date());
        this.save(adImage);

        //把最新列表发给机器
        syncImageList(frankMachineId);
    }

    /**
     * 删除图片
     *
     * @param adImageId
     */
    @Override
    public void delImage(Integer adImageId) {

        //删除数据库内容
        LambdaQueryWrapper<AdImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdImage::getId, adImageId);

        AdImage bean = baseMapper.selectOne(wrapper);
        String imagePath = bean.getImagePath();
        String frankMachineId = bean.getFrankMachineId();

        this.baseMapper.delete(wrapper);

        //删除本地文件
        String[] split = imagePath.split("/");
        int length = split.length;
        String dirName = split[length-2];
        String fileName = split[length -1];
        String filePath = basePath + parentDirName + "\\" + dirName + "\\" + fileName;
        log.info(filePath);
        FileUtil.del(filePath);

        //通知机器更新
        syncImageList(frankMachineId);
    }

    @Override
    public void syncImageList(String frankMachineId) {
        //判断打印任务是否在进行中
        if (!printJobService.checkPrintJobFinish(frankMachineId)){
            throw new FebsException(frankMachineId + "的打印任务没有完成，请勿操作");
        }

        AdImageInfo[] adImageInfoArr = getAdImageInfoArr(frankMachineId);
        if (adImageInfoArr.length > 0){
            try {
                serviceToMachineProtocol.syncImageList(frankMachineId, adImageInfoArr);
            }catch (Exception e){
                log.info("发给机器出错了" + e.getMessage());
            }
        }
    }

    /**
     * 根据ID搜索有几个图片
     * @param frankMachineId
     * @return
     */
    @Override
    public int selectCout(String frankMachineId) {
        LambdaQueryWrapper<AdImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdImage::getFrankMachineId, frankMachineId);
        return this.baseMapper.selectCount(wrapper);
    }
}
