package com.ljm.demo.service;

import com.ljm.demo.model.User;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/20 22:18
 * @description
 **/
public interface UserService {

    User findUserById(Integer id);
}
