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
public class EsPageDO implements Serializable {


    private static final long serialVersionUID = -5324190946499353605L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 楼层名称
     */
    private String pageName;
    /**
     * 楼层数据
     */
    private String pageData;
    /**
     * 页面类型
     */
    private String pageType;
    /**
     * 客户端类型
     */
    private String clientType;

}
