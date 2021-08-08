package com.ljm.spring.formework.bean;


import com.ljm.spring.formework.context.MyAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/24 21:26
 * @description
 **/
public class MyDefaultListableBeanFactory extends MyAbstractApplicationContext {

    /**
     * 存储注册信息的BeanDefinition
     */
    protected final Map<String, MyBeanDefinition> beanDefinitionMap=new ConcurrentHashMap<>();
}
