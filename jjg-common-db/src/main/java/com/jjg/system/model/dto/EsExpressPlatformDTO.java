package com.jjg.system.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsExpressPlatformDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 快递平台名称
     */
    private String name;

    /**
     * 是否开启快递平台,1开启，0未开启
     */
    private Integer isOpen;

    /**
     * 快递平台配置
     */
    private String config;

    /**
     * 快递平台beanid
     */
    private String bean;

}
