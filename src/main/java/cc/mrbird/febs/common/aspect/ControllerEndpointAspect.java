package cc.mrbird.febs.common.aspect;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.monitor.service.ILogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ControllerEndpointAspect extends BaseAspectSupport {
    @Autowired
    MessageUtils messageUtils;

    private final ILogService logService;

    @Pointcut("@annotation(cc.mrbird.febs.common.annotation.ControllerEndpoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws FebsException {
        Object result;
        Method targetMethod = resolveMethod(point);
        ControllerEndpoint annotation = targetMethod.getAnnotation(ControllerEndpoint.class);
        String operation = annotation.operation();
        long start = System.currentTimeMillis();
        try {
            result = point.proceed();
            if (StringUtils.isNotBlank(operation)) {
                RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) attributes;
                String ip = StringUtils.EMPTY;
                if (servletRequestAttributes != null) {
                    ip = servletRequestAttributes.getRequest().getRemoteAddr();
                }
//                logService.saveLog(point, FebsUtil.getCurrentUser(), targetMethod, ip, operation, start);
            }
            return result;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
            String exceptionMessage = annotation.exceptionMessage();
            String message = throwable.getMessage();
            if (exceptionMessage.contains("{")){
                exceptionMessage = exceptionMessage.substring(1,exceptionMessage.length() - 1);
                exceptionMessage = messageUtils.getMessage(exceptionMessage);
            }
//            String error = FebsUtil.containChinese(message) ? exceptionMessage + "???" + message : exceptionMessage;
            throw new FebsException(message);
        }
    }
}



