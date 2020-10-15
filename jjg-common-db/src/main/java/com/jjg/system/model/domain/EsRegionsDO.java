package com.jjg.system.model.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsRegionsDO implements Serializable {

    private static final long serialVersionUID = 8362975487394905145L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 父地区id
     */
    private Long parentId;
    /**
     * 路径
     */
    private String regionPath;
    /**
     * 级别
     */
    private Integer regionGrade;
    /**
     * 名称
     */
    private String localName;
    /**
     * 邮编
     */
    private String zipcode;

    /**
     * 子对象集合
     */
    private List<EsRegionsDO> children;
}
