package com.ljm.spring.formework.context;


import com.ljm.spring.formework.annotation.MyAutowired;
import com.ljm.spring.formework.bean.MyBeanDefinition;
import com.ljm.spring.formework.bean.MyBeanDefinitionReader;
import com.ljm.spring.formework.bean.MyBeanWrapper;
import com.ljm.spring.formework.bean.MyDefaultListableBeanFactory;
import com.ljm.spring.formework.bean.MyBeanFactory;
import com.ljm.spring.formework.utils.ClassUtils;
import com.ljm.spring.formework.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/24 21:28
 * @description 应用程序上下文环境创建、获取、管理bea
 **/
public class MyApplicationContext extends MyDefaultListableBeanFactory implements MyBeanFactory {

    private String[] configLocations;
    private MyBeanDefinitionReader reader;
    private Properties config;

    /**
     *ioc容器, 存放需要由spring管理的Bean实例
     */
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    /**
     * 存放Bean包装器信息
     */
    private Map<String, MyBeanWrapper> factoryBeanInstanceCache = new HashMap<>();

    public Map<String, Object> getFactoryBeanObjectCache() {
        return factoryBeanObjectCache;
    }


    public Properties getConfig() {
        return config;
    }

    public MyApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        try {
            MyBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
            //重ioc容器中获取bean实例
            Object instance = instantiateBean(beanDefinition);
            if (null == instance) {
                return null;
            }
            //把bean实例放入包装器中
            MyBeanWrapper beanWrapper = new MyBeanWrapper(instance);
            this.factoryBeanInstanceCache.put(beanName, beanWrapper);
            //依赖注入
            populateBean(instance);
            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 注入依赖的Bean
     */
    private void populateBean(Object instance) {
        Class clas = instance.getClass();
        Field[] fields = clas.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(MyAutowired.class)) {
                continue;
            }
            MyAutowired autowired = field.getAnnotation(MyAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = StringUtils.toLowerFirstWord(field.getType().getSimpleName());
            }
            field.setAccessible(true);
            try {
                boolean insertBeanDefinition = false;
                MyBeanDefinition myBeanDefinition;
                if (super.beanDefinitionMap.containsKey(autowiredBeanName)) {
                    // 当 @MyAutowired UserServiceImpl重ioc容器获取bean
                    myBeanDefinition = super.beanDefinitionMap.get(autowiredBeanName);
                } else {
                    //当 @MyAutowired UserService,则找到接口的实现类封装成bean修饰器然后再注入bean实例
                    String subClassName = ClassUtils.getInterfaceImpl(reader.getPackageName(), field.getType());
                    myBeanDefinition = super.beanDefinitionMap.get(subClassName);
                    insertBeanDefinition = true;
                }
                if (myBeanDefinition == null) {
                    continue;
                }
                Object autowiredInstance = instantiateBean(myBeanDefinition);
                if (null == instance) {
                    continue;
                }
                if (insertBeanDefinition) {
                    //把UserService和UserServiceImpl对应的修饰器类对应上,避免别的地方依赖注入的时候又重新调用一次获取接口子类的逻辑
                    super.beanDefinitionMap.put(autowiredBeanName, myBeanDefinition);
                }
                field.set(instance, autowiredInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 传入一个BeanFefinition返回一个实例,如果实例存在,重ioc容器中获取
     * 使用到了 【注册式单例模式】
     */
    private Object instantiateBean(MyBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {
            //根据className确定类是否有示例
            if (this.factoryBeanObjectCache.containsKey(className)) {
                instance = this.factoryBeanObjectCache.get(className);
            } else {
                Class<?> clas = Class.forName(className);
                instance = clas.newInstance();
                this.factoryBeanObjectCache.put(className, instance);
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return this.getBean(beanClass.getName());
    }

    @Override
    public void refresh() throws Exception {
        //1.定位配置文件
        reader = new MyBeanDefinitionReader(this.configLocations);
        this.config = reader.getConfig();
        //2.加载配置文件,扫描相关的类,把它们封装成BeanDefinition
        List<MyBeanDefinition> beanDefinitionList = reader.loadBeanDefinitions();
        //3.把bean信息放入到map中
        registerBeanFefinition(beanDefinitionList);
        //4.控制反转和依赖注入
        autowrited();
    }

    private void autowrited() {
        for (Map.Entry<String, MyBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            try {
                getBean(beanName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void registerBeanFefinition(List<MyBeanDefinition> beanDefinitionList) throws Exception {
        for (MyBeanDefinition beanDefinition : beanDefinitionList) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getBeanName())) {
                throw new Exception("The " + beanDefinition.getBeanName() + " is exists");
            }
            super.beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
        }
    }
}
