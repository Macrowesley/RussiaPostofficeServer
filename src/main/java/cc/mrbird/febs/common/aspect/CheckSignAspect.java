package cc.mrbird.febs.common.aspect;

import cc.mrbird.febs.common.utils.JoinPointUtil;
import cc.mrbird.febs.common.utils.MD5Util;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.*;

@Aspect   //定义一个切面
@Configuration
@Log4j2
public class CheckSignAspect {
    private final static String PRIVATE_KEY = "QKBgQCWlA";
    // 定义切点Pointcut
    @Pointcut("@annotation(cc.mrbird.febs.common.annotation.CheckSign)")
    public void excudeService() {
    }


    private static ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private static ThreadLocal<String> key = new ThreadLocal<String>();
    private static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 请求方法前打印内容
     * 得到所有参数信息
     * 判断签名是否一致
     * 判断时间是佛过期 30s
     * 判断redis是否存在随机数
     * @param joinPoint
     */
    @Before("excudeService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("doBefore ");
        // 请求开始时间
        startTime.set(System.currentTimeMillis());
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        // 获取请求头
        Map<String, String>  headersInfo = JoinPointUtil.getHeadersInfo(request);
        log.info("头部信息 {}" ,headersInfo);
        log.info("nonce = {}", headersInfo.get("nonce"));
        log.info("sign = {}", headersInfo.get("sign"));



        //获取方法类型
        String method = request.getMethod();
        StringBuffer params = new StringBuffer();
        if (HttpMethod.GET.toString().equals(method)) {// get请求

            String queryString = request.getQueryString();
            if (StringUtils.isEmpty(queryString)){
                //处理Restful
                log.info("get Restful 请求");
                Object[] paramsArray = joinPoint.getArgs();
                if (paramsArray != null && paramsArray.length > 0) {
                    for (int i = 0; i < paramsArray.length; i++) {
                        if (paramsArray[i] instanceof Serializable) {
                            log.info("get Restful paramsArray[{}}].toString() = {}", i, paramsArray[i].toString());
                            params.append(paramsArray[i].toString()).append(",");
                        }
                    }
                }

               /* if (StringUtils.isNotBlank(queryString)) {
                    params.append(queryString).append("&privateKey="+PRIVATE_KEY);
                }*/
            }else{
                //处理URLParame
                log.info("get &参数 请求");
                Map<String, String> parameterMap = JoinPointUtil.getURLParameterMap(request);
                log.info("get参数信息 {}" ,parameterMap);
            }

        } else {//其他请求

            log.info("其他请求");
            Object[] paramsArray = joinPoint.getArgs();
            if (paramsArray != null && paramsArray.length > 0) {
                for (int i = 0; i < paramsArray.length; i++) {
                    if (paramsArray[i] instanceof Serializable) {
                        log.info("paramsArray[{}}].toString() = {}",i,paramsArray[i].toString());
                        params.append(paramsArray[i].toString()).append(",");
                    } else {
                        //这里针对post的json对象
                        //使用json系列化 反射等等方法 反系列化会影响请求性能建议重写tostring方法实现系列化接口
                        try {
                            String param= objectMapper.writeValueAsString(paramsArray[i]);
                            if(StringUtils.isNotBlank(param)) {
                                JoinPointUtil.getJsonStrMap(param);
                                params.append(param).append(",");
                                log.info("param = {}",param);
                            }

                        } catch (JsonProcessingException e) {
                            log.error("doBefore obj to json exception obj={},msg={}",paramsArray[i],e);
                        }
                    }
                }

                params.append("&privateKey="+PRIVATE_KEY);
            }
        }
        String resSign =  MD5Util.MD5Encode("name=好看&nonce=1607397527135&privateKey=QKBgQCWlA&sex=男");
        if ("E99F6C920C4F26483B0C1DF07A11E3B3".equals(resSign)){
            log.info("前后端加密一致");
        }else{
            log.info("前后端加密不一致 resSign = {}",resSign);
        }

        log.info("request params>>>>>>uri={} method={},params={}", request.getRequestURI(), method, params);
        log.info("");
    }


 /*   @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        String class_name = pjp.getTarget().getClass().getName();
        String method_name = pjp.getSignature().getName();

        String[] paramNames = getFieldsName(class_name, method_name);
        Object[] method_args = pjp.getArgs();
        SortedMap<String, String> map = logParam(paramNames, method_args);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("获取RequestBody");
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("REQUEST：" + JSONObject.toJSONString(pjp.getArgs()));


        if (map != null) {
            String sign = map.get("sign").toUpperCase();
            map.remove("sign");
            String realSign = SignUtil.createSign("utf8", map);
            if (!realSign.equals(sign)) {
                return "签名校验错误";
            }
        }
        Object result = pjp.proceed();
        return result;
    }*/

    /**
     * 使用javassist来获取方法参数名称
     *
     * @param class_name  类名
     * @param method_name 方法名
     * @return
     * @throws Exception
     */
    private String[] getFieldsName(String class_name, String method_name) throws Exception {
        Class<?> clazz = Class.forName(class_name);
        String clazz_name = clazz.getName();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(clazz);
        pool.insertClassPath(classPath);

        CtClass ctClass = pool.get(clazz_name);
        CtMethod ctMethod = ctClass.getDeclaredMethod(method_name);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            return null;
        }
        String[] paramsArgsName = new String[ctMethod.getParameterTypes().length];
        int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramsArgsName.length; i++) {
            paramsArgsName[i] = attr.variableName(i + pos);
        }
        return paramsArgsName;
    }

    /**
     * 打印方法参数值  基本类型直接打印，非基本类型需要重写toString方法
     *
     * @param paramsArgsName  方法参数名数组
     * @param paramsArgsValue 方法参数值数组
     */
    private SortedMap<String, String> logParam(String[] paramsArgsName, Object[] paramsArgsValue) {
        Map<String, String> map = new HashMap();
        if (ArrayUtils.isEmpty(paramsArgsName) || ArrayUtils.isEmpty(paramsArgsValue)) {
            log.info("该方法没有参数");
            return null;
        }
//        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < paramsArgsName.length; i++) {
            //参数名
            String name = paramsArgsName[i];
            //参数值
            Object value = paramsArgsValue[i];

            if (isPrimite(value.getClass())) {
                map.put(name, String.valueOf(value));
            } else {
                map.put(name, value.toString());
            }
        }
        log.info("打印所有参数");
        log.info(map);
        return new TreeMap<>(map);
    }

    /**
     * 判断是否为基本类型：包括String
     *
     * @param clazz clazz
     * @return true：是;     false：不是
     */
    private boolean isPrimite(Class<?> clazz) {
        if (clazz.isPrimitive() || clazz == String.class) {
            return true;
        } else {
            return false;
        }
    }
}