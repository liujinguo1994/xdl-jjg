package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 第二件半价活动表Form
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsHalfPriceForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private Long startTime;
    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private Long endTime;

    /**
     * 活动标题
     */
    @ApiModelProperty(value = "活动标题")
    private String title;

    /**
     * 商品参与方式1全部商品：2，部分商品
     */
    @ApiModelProperty(value = "商品参与方式1全部商品：2，部分商品")
    private Integer rangeType;


    /**
     * 活动说明
     */
    @ApiModelProperty(value = "活动说明")
    private String description;

    @ApiModelProperty(value = "参与活动商品集合")
    private List<EsPromotionGoodsForm> goodsList;


}
