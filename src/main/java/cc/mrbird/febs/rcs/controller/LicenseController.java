package cc.mrbird.febs.rcs.controller;

import cc.mrbird.febs.common.license.AbstractServerInfos;
import cc.mrbird.febs.common.license.LicenseVerifyUtils;
import cc.mrbird.febs.common.license.LinuxServerInfos;
import cc.mrbird.febs.common.license.WindowsServerInfos;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller
@RequestMapping("/license")
@ApiIgnore
public class LicenseController {
    @Autowired
    LicenseVerifyUtils verifyUtils;

    /**
     * 获取服务器硬件信息
     */
    @GetMapping("/getServerInfos")
    @ResponseBody
    public String getServerInfos(@RequestParam(value = "osName", required = false) String osName) {
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        String info = JSON.toJSONString(abstractServerInfos.getServerInfos(), SerializerFeature.DisableCircularReferenceDetect);
        log.info("info = " + info);
        return info;
    }

    @GetMapping("/reinstall")
    @ResponseBody
    public String reInstall() {
        return verifyUtils.install() ? "success" : "fail";
    }


}
