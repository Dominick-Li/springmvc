package com.ljm.spring.formework.utils;

import com.ljm.demo.service.UserService;
import org.reflections.Reflections;

import java.util.Set;

/**
 * @author Dominick Li
 * @CreateTime 2021/8/4 22:45
 * @description
 **/
public class ClassUtils {

    /**
     * 获取接口的实现类
     */
    public static String getInterfaceImpl(String packageName, Class classz) {
        Reflections reflections = new Reflections(packageName);
        Set<? extends Class> classes = reflections.getSubTypesOf(classz);
        String beanClassName = null;
        for (Class clazz : classes) {
            beanClassName = clazz.getSimpleName();
            beanClassName=StringUtils.toLowerFirstWord(beanClassName);
            break;
        }
        return beanClassName;
    }

    public static void main(String[] args) {
        String beanClassName=getInterfaceImpl("com.ljm.demo", UserService.class);
        System.out.println(beanClassName);
    }
}
