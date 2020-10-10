package com.xdl.jjg.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Array工具类
 */
public class ArrayUtil {

    /**
     * @param arr
     * @param targetValue
     * @return
     */
    public static boolean isExistenceUseList(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 判断数组中某个元素是否存在(用Set)
     *
     * @param arr
     * @param targetValue
     * @return
     */
    public static boolean isExistenceUseSet(String[] arr, String targetValue) {
        Set<String> set = new HashSet<String>(Arrays.asList(arr));
        return set.contains(targetValue);
    }

    /**
     * 判断数组中某个元素是否存在(用循环)
     *
     * @param arr
     * @param targetValue
     * @return
     */
    public static boolean isExistenceUseLoop(String[] arr, String targetValue) {
        for (String s : arr) {
            if (s.equals(targetValue))
                return true;
        }
        return false;
    }
    public static boolean isNumeric(String str) {
        try {
            String bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        return true;
    }
}
