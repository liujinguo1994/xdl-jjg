package com.xdl.jjg.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class EsGoodGoodsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "goodsName")
    private String goodsName;

    /**
     * 商品链接
     */
    @ApiModelProperty(value = "goodsUrl")
    private String goodsUrl;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "picUrl")
    private String picUrl;

    /**
     * 状态(1:待发布,2.已发布,3:已下架)
     */
    @ApiModelProperty(value = "state")
    private Integer state;

    /**
     * 下架原因
     */
    @ApiModelProperty(value = "underMessage")
    private String underMessage;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "createTime")
    private Long createTime;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "updateTime")
    private Long updateTime;

}
