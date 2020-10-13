package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 品质好货
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-08-06
 */
@Data
public class EsGoodGoodsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品链接
     */
    private String goodsUrl;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 状态(1:待发布,2.已发布,3:已下架)
     */
    private Integer state;

    /**
     * 下架原因
     */
    private String underMessage;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后修改时间
     */
    private Long updateTime;

}
