package com.ljm.spring.formework.annotation;

import java.lang.annotation.*;

/**
 * @author Dominick Li
 * @CreateTime 2021/8/4 21:07
 * @description 标识控制器的所有方法返回json数据视图
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRestController {
}