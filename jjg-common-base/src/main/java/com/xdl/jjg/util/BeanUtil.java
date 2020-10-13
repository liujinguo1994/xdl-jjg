package com.xdl.jjg.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象之间转换操作
 */
public class BeanUtil {

    /**
     * 根据Map中的key对应对象的属性来更新对象的值
     * <li>
     * updateProperties中的key必须跟bean中的字段名保持一致才能更新
     * </li>
     *
     * @param updateProperties 要更新的字段以及值
     * @param bean             要更新的对象
     * @author wangyijie
     */
    public static <T> void copyPropertiesInclude(Map<String, Object> updateProperties, T bean) {

        Set<Map.Entry<String, Object>> revisabilityFiledSet = updateProperties.entrySet();
        for (Map.Entry<String, Object> entry : revisabilityFiledSet) {
            Object value = entry.getValue();
            if (value != null) {
                try {
                    org.apache.commons.beanutils.BeanUtils.setProperty(bean, entry.getKey(), value);
                } catch (Exception e) {

                }
            }
        }

    }

    /**
     * 复制属性
     *
     * @param objectFrom
     * @param objectTo
     */
    public static void copyProperties(Object objectFrom, Object objectTo) {
        BeanUtils.copyProperties(objectFrom, objectTo, getNullPropertyNames(objectFrom));
    }

    /**
     * 过滤null值
     *
     * @param source
     * @return
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 对象转换
     *
     * @param source
     * @param targetBean
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List<?> source, Class<T> targetBean) {
        try {
            String sourceStr = JsonUtil.objectToJson(source);
            return JsonUtil.jsonToList(sourceStr, targetBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
