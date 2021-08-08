package com.ljm.demo.controller;

import com.ljm.demo.model.User;
import com.ljm.demo.service.UserService;
import com.ljm.spring.formework.annotation.MyAutowired;
import com.ljm.spring.formework.annotation.MyRequestMapping;
import com.ljm.spring.formework.annotation.MyRequestParam;
import com.ljm.spring.formework.annotation.MyRestController;


/**
 * @author Dominick Li
 * @CreateTime 2021/1/20 22:20
 * @description
 **/
@MyRestController
@MyRequestMapping("/user")
public class UserController {
    @MyAutowired
    private UserService userService;

    @MyRequestMapping("/get")
    //@MyResponseBody
    public User findUserById(@MyRequestParam(value = "id") Integer id){
        return userService.findUserById(id);
    }
}
