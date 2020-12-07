package cc.mrbird.febs.common.aspect;

import cc.mrbird.febs.common.utils.SignUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Aspect   //定义一个切面
@Configuration
@Log4j2
public class CheckSignAspectBackup {
    // 定义切点Pointcut
    @Pointcut("@annotation(cc.mrbird.febs.common.annotation.CheckSignTest)")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        String class_name = pjp.getTarget().getClass().getName();
        String method_name = pjp.getSignature().getName();
        String[] paramNames = getFieldsName(class_name, method_name);
        Object[] method_args = pjp.getArgs();
        SortedMap<String, String> map = logParam(paramNames, method_args);
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
    }

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
//            if ("sign".equals(name)){
//                continue;
//            }
            if (isPrimite(value.getClass())) {
                map.put(name, String.valueOf(value));
            } else {
                map.put(name, value.toString());
            }
        }
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