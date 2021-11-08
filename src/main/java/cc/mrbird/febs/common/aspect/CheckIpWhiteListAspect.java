package cc.mrbird.febs.common.aspect;

import cc.mrbird.febs.common.configure.IpConfig;
import cc.mrbird.febs.common.utils.HttpContextUtil;
import cc.mrbird.febs.common.utils.IpUtil;
import cc.mrbird.febs.rcs.common.enums.RcsApiErrorEnum;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CheckIpWhiteListAspect {
    @Autowired
    IpConfig ipConfig;

    // 定义切点Pointcut
    @Pointcut("@annotation(cc.mrbird.febs.common.annotation.CheckIpWhiteList)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint point){
        log.info("开始检验ip白名单");
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IpUtil.getIpAddr(request);
        log.info("ip = " + ip);

        if (!IpUtil.checkIsRussiaIp(ip, ipConfig.getWhiteIps())){
            log.error("{}不是俄罗斯ip", ip);
            //TODO 真正需要开启ip判断的时候，把报错用起来
//            throw new RcsApiException(RcsApiErrorEnum.IpIsNotInWhiteList);
        }

    }


}
