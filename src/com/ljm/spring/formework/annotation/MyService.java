package com.ljm.spring.formework.annotation;

import java.lang.annotation.*;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/20 22:15
 * @description 定义业务处理Bean
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyService {
    String value() default "";
}
