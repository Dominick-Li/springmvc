package com.ljm.demo.controller;

import com.ljm.spring.formework.annotation.MyController;
import com.ljm.spring.formework.annotation.MyRequestMapping;
import com.ljm.spring.formework.annotation.MyRequestParam;
import com.ljm.spring.formework.view.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/31 18:23
 * @description
 **/
@MyController
@MyRequestMapping("/")
public class IndexController {

    @MyRequestMapping("/index")
    public ModelAndView myTest(@MyRequestParam(value = "name") String name) {
        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        return new ModelAndView("index.html", model);
    }

}
