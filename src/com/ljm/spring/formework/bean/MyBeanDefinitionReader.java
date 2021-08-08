package com.ljm.spring.formework.bean;

import com.ljm.spring.formework.annotation.MyRestController;
import com.ljm.spring.formework.annotation.MyController;
import com.ljm.spring.formework.annotation.MyService;
import com.ljm.spring.formework.utils.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/24 21:30
 * @description 对配置文件查找, 读取, 解析
 **/
public class MyBeanDefinitionReader {


    private String packageName;

    private List<String> registyBeanClasses = new ArrayList<>();

    private Properties config = new Properties();

    public Properties getConfig() {
        return config;
    }

    public String getPackageName() {
        return packageName;
    }

    /**
     * 固定配置文件的key
     */
    private final String SCAN_PACKAGE = "scanPackage";

    public MyBeanDefinitionReader(String... localtions) {
        //通过URL定位到其所应的文件
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(localtions[0])) {
            config.load(is);
            scanner(config.getProperty(SCAN_PACKAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描需要被sprig管理包
     */
    private void scanner(String packageName) {
        if(this.packageName==null){
            this.packageName=packageName;
        }
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                //递归读取包
                scanner(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                //把bean的class信息放入到List中
                registyBeanClasses.add(className);
            }
        }
    }

    /**
     * 把配置文件扫描的所有配置信息转换未MYBeanDefinition对象
     */
    public List<MyBeanDefinition> loadBeanDefinitions() {
        List<MyBeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registyBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                //接口不需要处理
                if (beanClass.isInterface()) {
                    continue;
                }
                //只处理需要包含指定需要代理类的注解的类
                if(beanClass.isAnnotationPresent(MyController.class) || beanClass.isAnnotationPresent(MyRestController.class) ||  beanClass.isAnnotationPresent(MyService.class)){
                    result.add(createBeanDefinition(StringUtils.toLowerFirstWord(beanClass.getSimpleName()), beanClass.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private MyBeanDefinition createBeanDefinition(String beanName, String beanClassName) {
        MyBeanDefinition myBeanDefinition = new MyBeanDefinition();
        myBeanDefinition.setBeanName(beanName);
        myBeanDefinition.setBeanClassName(beanClassName);
        return myBeanDefinition;
    }

}
