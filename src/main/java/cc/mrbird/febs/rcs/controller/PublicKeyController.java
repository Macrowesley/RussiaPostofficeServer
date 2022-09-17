package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.service.IPublicKeyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 公钥表 Controller
 *
 * @author mrbird
 * @date 2021-04-17 14:45:23
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@ApiIgnore
public class PublicKeyController extends BaseController {

    @Autowired
    IPublicKeyService publicKeyService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "publicKey")
    public String publicKeyIndex(){
        return FebsUtil.view("publicKey/publicKey");
    }

    @GetMapping("publicKey")
    @ResponseBody
    @RequiresPermissions("publicKey:list")
    public FebsResponse getAllPublicKeys(PublicKey publicKey) {
        return new FebsResponse().success().data(publicKeyService.findPublicKeys(publicKey));
    }

    @GetMapping("publicKey/list")
    @ResponseBody
    @RequiresPermissions("publicKey:list")
    public FebsResponse publicKeyList(QueryRequest request, PublicKey publicKey) {
        Map<String, Object> dataTable = getDataTable(this.publicKeyService.findPublicKeys(request, publicKey));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增PublicKey", exceptionMessage = "新增PublicKey失败")
    @PostMapping("publicKey")
    @ResponseBody
    @RequiresPermissions("publicKey:add")
    public FebsResponse addPublicKey(@Valid PublicKey publicKey) {
        this.publicKeyService.createPublicKey(publicKey);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除PublicKey", exceptionMessage = "删除PublicKey失败")
    @GetMapping("publicKey/delete")
    @ResponseBody
    @RequiresPermissions("publicKey:delete")
    public FebsResponse deletePublicKey(PublicKey publicKey) {
        this.publicKeyService.deletePublicKey(publicKey);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PublicKey", exceptionMessage = "修改PublicKey失败")
    @PostMapping("publicKey/update")
    @ResponseBody
    @RequiresPermissions("publicKey:update")
    public FebsResponse updatePublicKey(PublicKey publicKey) {
        this.publicKeyService.updatePublicKey(publicKey);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PublicKey", exceptionMessage = "导出Excel失败")
    @PostMapping("publicKey/excel")
    @ResponseBody
    @RequiresPermissions("publicKey:export")
    public void export(QueryRequest queryRequest, PublicKey publicKey, HttpServletResponse response) {
        List<PublicKey> publicKeys = this.publicKeyService.findPublicKeys(queryRequest, publicKey).getRecords();
        //ExcelKit.$Export(PublicKey.class, response).downXlsx(publicKeys, false);
    }
}
