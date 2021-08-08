package com.ljm.spring.formework.utils;

/**
 * @author Dominick Li
 * @CreateTime 2021/8/4 22:07
 * @description
 **/
public class StringUtils {

    /**
     * 将字符串中的首字母小写
     */
    public static String toLowerFirstWord(String name) {
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }
}
