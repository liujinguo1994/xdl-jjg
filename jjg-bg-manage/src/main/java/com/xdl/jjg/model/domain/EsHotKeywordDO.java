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
public class EsHotKeywordDO implements Serializable {

    private static final long serialVersionUID = -5572328776172211369L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 文字内容
     */
    private String hotName;
    /**
     * 排序
     */
    private Integer sort;

}
