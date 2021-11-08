package cc.mrbird.febs.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要验证白名单的接口上添加这个注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME )
public @interface CheckIpWhiteList {
}
