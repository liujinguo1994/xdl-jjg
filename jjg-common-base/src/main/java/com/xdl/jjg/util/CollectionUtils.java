package com.xdl.jjg.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/9日
 * @Description 集合类
 */
public class CollectionUtils {


    public static Boolean isEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    public static Boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static <T> ArrayList<T> newArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<>();
        if (elements == null) {
            return list;
        }
        Collections.addAll(list, elements);
        return list;
    }


}
