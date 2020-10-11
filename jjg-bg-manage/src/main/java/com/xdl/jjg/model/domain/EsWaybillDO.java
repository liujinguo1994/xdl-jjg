package com.xdl.jjg.model.domain;

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
public class EsWaybillDO implements Serializable {

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
