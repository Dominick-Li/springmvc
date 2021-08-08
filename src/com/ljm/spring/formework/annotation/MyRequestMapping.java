package com.ljm.spring.formework.annotation;

import java.lang.annotation.*;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/20 22:14
 * @description 控制器请求URL配置
 **/
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {
    String value() default "";
}
