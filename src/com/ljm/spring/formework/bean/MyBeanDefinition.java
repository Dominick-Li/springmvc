package com.ljm.spring.formework.bean;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/24 21:18
 * @description 存储Bean的配置信息
 **/
public class MyBeanDefinition {
    /**
     * Bean的全类名
     */
    private String beanClassName;

    /**
     * Bean名称,在Ioc容器里存储的Key
     */
    private String beanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }


    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
