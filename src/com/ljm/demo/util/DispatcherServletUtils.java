package com.ljm.demo.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dominick Li
 * @CreateTime 2021/1/23 23:09
 * @description
 **/
public class DispatcherServletUtils {
    private DispatcherServletUtils(){}

    /**
     * 将字符串中的首字母小写
     */
    public static String toLowerFirstWord(String name) {
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }

    //参数缺失
    public static Object convert(Class<?> type, String[] value) {
        if (value == null || value.length == 0) {
            return null;
        }
        if (Integer.class == type) {
            return Integer.parseInt(value[0]);
        }
        if (Long.class == type) {
            return Long.parseLong(value[0]);
        }
        if (Double.class == type) {
            return Double.class;
        }
        if (String.class == type) {
            return value[0];
        }
        if (List.class == type) {
            return Arrays.asList(value);
        }
        return value;
    }

}
