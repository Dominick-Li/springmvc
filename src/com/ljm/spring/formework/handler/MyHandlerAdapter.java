package com.ljm.spring.formework.handler;

import com.ljm.demo.util.DispatcherServletUtils;
import com.ljm.spring.formework.annotation.MyResponseBody;
import com.ljm.spring.formework.annotation.MyRestController;
import com.ljm.spring.formework.exception.MissingParametersException;
import com.ljm.spring.formework.exception.ViewNotFindException;
import com.ljm.spring.formework.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/31 18:43
 * @description 处理请求并返回视图或者json数据 【使用的适配器设计模式】
 **/
public class MyHandlerAdapter {

    public boolean supports(Object handler) {
        return (handler instanceof MyHandlerMapping);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, MyHandlerMapping handler) throws Exception {
        //获取方法的参数列表
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();

        //保存参数值
        Object[] paramValues = new Object[parameterTypes.length];

        //获取请求的参数
        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (!handler.getParamIndexMapping().containsKey(entry.getKey())) {
                continue;
            }
            int index = handler.getParamIndexMapping().get(entry.getKey());
            paramValues[index] = DispatcherServletUtils.convert(parameterTypes[index], entry.getValue());
            if (handler.requiredList != null) {
                //重必选参数列表移除存在的key
                handler.requiredList.remove(entry.getKey());
            }
        }

        if (handler.requiredList != null && handler.requiredList.size() > 0) {
            throw new MissingParametersException(handler.requiredList.toString() + "是必选参数,不能为null");
        }

        if (handler.paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int index = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = request;
        }
        if (handler.paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int index = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = response;
        }
        if (handler.paramIndexMapping.containsKey(HttpSession.class.getName())) {
            int index = handler.paramIndexMapping.get(HttpSession.class.getName());
            paramValues[index] = request.getSession();
        }

            //利用反射机制来调用 第一个参数是method所对应的实例 在ioc容器中
            Object returnValue = handler.getMethod().invoke(handler.controller, paramValues);
            if (returnValue == null) {
                return null;
            }
            boolean isModelAndView = handler.getMethod().getReturnType() == ModelAndView.class;
            if (isModelAndView) {
                return (ModelAndView) returnValue;
            } else {
                //判断当前控制器是否包含MyRestController注解或者当前调用的方法是否包含MyResponseBody注解
                if(handler.getMethod().isAnnotationPresent(MyResponseBody.class) ||  handler.controller.getClass().isAnnotationPresent(MyRestController.class))
                {
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Content-Type", "text/html; charset=UTF-8");
                    response.getWriter().write(returnValue.toString());
                    return  null;
                }
                //抛出视图未找到异常
                throw  new ViewNotFindException(returnValue.toString());
            }

    }

}
