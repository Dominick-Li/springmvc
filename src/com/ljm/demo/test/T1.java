package com.ljm.demo.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/31 19:25
 * @description
 **/
public class T1 {
    private static final Pattern pattern = Pattern.compile("\\$\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
    public static void main(String[] args) {
        Matcher matcher= pattern.matcher("我好喜欢你 ${name}");
        if(matcher.find()){
            System.out.println(true);
        }else{
            System.out.println(false);
        }
        String paramName="${name}";
        paramName = paramName.replaceAll("\\$\\{|}", "");
        System.out.println(paramName);
    }
}
