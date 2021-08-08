package com.ljm.spring.formework.view;

import java.util.Map;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/31 19:10
 * @description 返回视图名称和页面需要的model数据
 **/
public class ModelAndView {
    private String ViewName;

    private Map<String, ?> model;

    public ModelAndView(String viewName, Map<String, ?> model) {
        ViewName = viewName;
        this.model = model;
    }

    public ModelAndView(String viewName) {
        ViewName = viewName;
    }

    public String getViewName() {
        return ViewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setViewName(String viewName) {
        ViewName = viewName;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
