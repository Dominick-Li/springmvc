package com.ljm.demo.service.impl;

import com.ljm.demo.model.User;
import com.ljm.demo.service.UserService;
import com.ljm.spring.formework.annotation.MyService;

import java.util.HashMap;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/20 22:19
 * @description
 **/
@MyService
public class UserServiceImpl implements UserService {

    HashMap<Integer,User> userHashMap=new HashMap<>();
    {
        userHashMap.put(1,new User("test","123"));
        userHashMap.put(2,new User("admin","456"));
    }

    @Override
    public User findUserById(Integer id){
        return userHashMap.get(id);
    }
}
