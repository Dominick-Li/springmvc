package com.ljm.demo.controller;

import com.ljm.spring.formework.annotation.MyController;
import com.ljm.spring.formework.annotation.MyRequestMapping;
import com.ljm.spring.formework.annotation.MyRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/20 22:20
 * @description 测试视图没找到异常
 **/
@MyController
@MyRequestMapping("/test")
public class TestController {

    //  test?param=12a
    @MyRequestMapping
    public String myTest(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
                         @MyRequestParam(value = "param") String param,@MyRequestParam(value = "array",required = false) List arrays) {
        System.out.println(param+","+arrays);
        return "请求成功";
    }

}
