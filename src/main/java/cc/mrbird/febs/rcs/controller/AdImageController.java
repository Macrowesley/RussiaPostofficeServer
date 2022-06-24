package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.rcs.Req.AdImageAddReq;
import cc.mrbird.febs.rcs.entity.AdImage;
import cc.mrbird.febs.rcs.service.IAdImageService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Api(value = "ad image controller")
@Slf4j
@Validated
@RestController
@RequestMapping("/adImage/")
@RequiredArgsConstructor
public class AdImageController extends BaseController {

    @Autowired
    IAdImageService adImageService;


    /**
     * 图片列表&搜索
     * @param request
     * @param bean
     * @return
     */
    @ApiOperation("显示图片列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "frankMachineId", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = AdImage.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @PostMapping("list")
//    @RequiresPermissions("adImage:list")
    @ControllerEndpoint(operation = "显示图片列表", exceptionMessage = "显示列表失败")
    public FebsResponse list(QueryRequest request,  AdImage bean){
        Map<String, Object> dataTable = getDataTable(this.adImageService.list(request, bean));
        return new FebsResponse().success().data(dataTable);
    }


    /**
     * 上传图片
     * @param mf
     * @return
     */
    @ApiOperation("上传图片")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @PostMapping("upload")
//    @RequiresPermissions("adImage:update")
    @ControllerEndpoint(operation = "上传图片", exceptionMessage = "上传图片失败")
    public FebsResponse upload(@ApiParam(name = "file", required = true) @RequestParam("file") MultipartFile mf){
        String uploadUrl = adImageService.upload(mf);
        return new FebsResponse().success().data(uploadUrl);
    }

    /**
     * 添加图片
     * @return
     */
    @ApiOperation("添加图片")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @PostMapping("add")
//    @RequiresPermissions("adImage:update")
    @ControllerEndpoint(operation = "添加图片", exceptionMessage = "添加图片失败")
    public FebsResponse addImage(@Validated @RequestBody AdImageAddReq adImageAddReq){
        adImageService.addImage(adImageAddReq);
        return new FebsResponse().success();
    }

    /**
     * 删除图片
     * @param id
     * @return
     */
    @ApiOperation("删除图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adImageId", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @PostMapping("delete/{adImageId}")
//    @RequiresPermissions("adImage:update")
    @ControllerEndpoint(operation = "删除图片", exceptionMessage = "删除图片失败")
    public FebsResponse delImage(@PathVariable Integer adImageId){
        adImageService.delImage(adImageId);
        return new FebsResponse().success();
    }

    /**
     * 同步图片列表给机器
     * @param frankMachineId
     * @return
     */
    @ApiOperation("同步图片列表给机器")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "frankMachineId", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = String.class),
            @ApiResponse(code = 500, message = "内部异常")
    })
    @GetMapping("syncAdImageList/{frankMachineId}")
//    @RequiresPermissions("adImage:update")
    @ControllerEndpoint(operation = "同步图片列表给机器", exceptionMessage = "同步图片列表给机器")
    public FebsResponse syncImageList(@PathVariable String frankMachineId){
        adImageService.syncImageList(frankMachineId);
        return new FebsResponse().success();
    }

}
