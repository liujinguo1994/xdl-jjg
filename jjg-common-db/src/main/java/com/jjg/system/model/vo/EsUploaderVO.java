package com.jjg.member.model.vo;

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
public class EsUploaderVO implements Serializable {

    private static final long serialVersionUID = -5667512661583700907L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 存储名称
     */
    private String name;
    /**
     * 是否开启
     */
    private Integer open;
    /**
     * 存储配置
     */
    private String config;
    /**
     * 存储插件id
     */
    private String bean;

}
