package com.ljm.spring.formework.exception;

/**
 * @author Dominick Li
 * @CreateTime 2021/8/4 23:14
 * @description
 **/
public class ViewNotFindException extends Exception {
    public ViewNotFindException() { }
    public ViewNotFindException(String message) {
        super(String.format("没有找到 [%s.html] 文件",message));
    }
}
