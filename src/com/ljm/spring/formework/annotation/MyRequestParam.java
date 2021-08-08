package com.ljm.spring.formework.annotation;

import java.lang.annotation.*;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/20 22:14
 * @description 请求参数映射
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface MyRequestParam {
    String value() default "";
    boolean required() default true;
}
