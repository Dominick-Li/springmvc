package com.ljm.spring.formework.annotation;

import java.lang.annotation.*;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/20 22:12
 * @description 实现依赖注入
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAutowired {
    String value() default "";
}
