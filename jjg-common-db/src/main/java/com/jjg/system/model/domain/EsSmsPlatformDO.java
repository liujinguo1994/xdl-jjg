package com.jjg.system.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsSmsPlatformDO implements Serializable {

    private static final long serialVersionUID = 5692411607650564355L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 平台名称
     */
    private String name;
    /**
     * 是否开启
     */
    private Integer open;
    /**
     * 配置
     */
    private String config;
    /**
     * bean
     */
    private String bean;

}
