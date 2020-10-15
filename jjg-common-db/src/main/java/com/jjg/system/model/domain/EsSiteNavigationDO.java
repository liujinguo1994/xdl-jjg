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
public class EsSiteNavigationDO implements Serializable {

    private static final long serialVersionUID = 349771739453781318L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 导航名称
     */
    private String navigationName;
    /**
     * 导航地址
     */
    private String url;
    /**
     * 客户端类型
     */
    private String clientType;
    /**
     * 图片
     */
    private String image;
    /**
     * 排序
     */
    private Integer sort;


}
