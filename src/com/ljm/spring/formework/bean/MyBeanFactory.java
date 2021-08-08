package com.ljm.spring.formework.bean;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/24 21:17
 * @description 单例工厂顶层设计
 **/
public interface MyBeanFactory {
    /**
     *根据bean名称获取实例
     */
    Object getBean(String beanName) throws Exception;
    /**
     *根据bean类名获取实例
     */
    Object getBean(Class<?> beanClass) throws Exception;
}
