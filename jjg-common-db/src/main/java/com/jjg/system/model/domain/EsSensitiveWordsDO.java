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
public class EsSensitiveWordsDO implements Serializable {

    private static final long serialVersionUID = 657373142665481331L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 敏感词名称
     */
    private String wordName;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 删除状态  0正常 1 删除
     */
    private Integer isDel;

}
