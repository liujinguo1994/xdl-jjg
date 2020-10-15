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
public class EsWaybillDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否开启
     */
    private Integer open;

    /**
     * 电子面单配置
     */
    private String config;

    /**
     * 电子面单bean
     */
    private String bean;


}
