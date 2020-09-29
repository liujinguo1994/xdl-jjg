package com.xdl.jjg.annotation;

import java.lang.annotation.*;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/9/18 14:45
 * 锁的参数
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheParam {

    /**
     * 字段名称
     *
     * @return String
     */
    String name() default "";

}
