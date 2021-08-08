package com.ljm.spring.formework.handler;

import com.ljm.spring.formework.annotation.MyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/31 18:45
 * @description 管理
 **/
public class MyHandlerMapping {
    //保存方法对应的实例
    protected Object controller;
    //保存映射的方法
    protected Method method;
    //参数顺序
    protected Map<String, Integer> paramIndexMapping;
    //必选参数列表吧
    protected List<String> requiredList;

    public Method getMethod() {
        return method;
    }

    public Map<String, Integer> getParamIndexMapping() {
        return paramIndexMapping;
    }

    /**
     * 构建一个Handler的基本参数
     */
    public MyHandlerMapping(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        initParamIndexMapping(method);
    }

    private void initParamIndexMapping(Method method) {
        paramIndexMapping = new HashMap<>();
        requiredList = new ArrayList<>();
        //提取方法注解参数
        Annotation[][] param = method.getParameterAnnotations();
        MyRequestParam mrp;
        for (int i = 0; i < param.length; i++) {
            for (Annotation a : param[i]) {
                if (a instanceof MyRequestParam) {
                    mrp = ((MyRequestParam) a);
                    String paramName = mrp.value();
                    if (!"".equals(paramName.trim())) {
                        paramIndexMapping.put(paramName, i);
                    }
                    if (mrp.required()) {
                        requiredList.add(paramName);
                    }
                }
            }
        }
        if (requiredList.size() == 0) {
            requiredList = null;
        }
        //提取方法中的request,response,session参数
        Class<?> paramsTypes[] = method.getParameterTypes();
        for (int i = 0; i < paramsTypes.length; i++) {
            Class<?> type = paramsTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class || type == HttpSession.class) {
                paramIndexMapping.put(type.getName(), i);
            }
        }
    }
}
