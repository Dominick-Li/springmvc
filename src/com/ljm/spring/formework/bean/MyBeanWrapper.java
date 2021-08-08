package com.ljm.spring.formework.bean;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/24 21:21
 * @description 包装代理对象
 **/
public class MyBeanWrapper {
    /**
     * 包装需要代理实例
     */
    private Object wrappedInstance;
    /**
     * 包装需要代理对象的class
     */
    private Class<?> wrappedClass;
    public MyBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass=wrappedInstance.getClass();
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }
    public Class<?> getWrappedClass() {
        return wrappedInstance.getClass();
    }


}
