package com.ljm.demo.model;

/**
 * @author Dominick Li
 * @CreateTime 2021/8/4 23:30
 * @description
 **/
public class User {

    private String username;
    private String password;

    public User(String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
