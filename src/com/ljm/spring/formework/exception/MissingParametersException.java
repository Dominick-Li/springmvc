package com.ljm.spring.formework.exception;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/23 23:22
 * @description
 **/
public class MissingParametersException extends Exception {
    public MissingParametersException() { }
    public MissingParametersException(String message) {
        super(message);
    }
}
