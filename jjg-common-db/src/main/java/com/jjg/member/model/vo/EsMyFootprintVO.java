package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员足迹表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-16 10:54:45
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMyFootprintVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(required = false, value = "主键ID")
    private Long id;

    /**
     * 商品ID
     */
    @ApiModelProperty(required = false, value = "商品ID")
    private Long goodsId;

    /**
     * 访问时间
     */
    @ApiModelProperty(required = false, value = "访问时间")
    private Long createTime;

    /**
     * 店铺ID
     */
    @ApiModelProperty(required = false, value = "店铺ID")
    private Long shopId;

    /**
     * 商品价格
     */
    @ApiModelProperty(required = false, value = "商品价格")
    private Double money;

    /**
     * 商品图片
     */
    @ApiModelProperty(required = false, value = "商品图片")
    private String img;

    /**
     * 会员ID
     */
    @ApiModelProperty(required = false, value = "会员ID")
    private Long memberId;
    /**
     * 时间
     */
    @ApiModelProperty(required = false, value = "时间")
    private String viewTime;

    /**
     * 商品名称
     */
    @ApiModelProperty(required = false, value = "商品名称")
    private String goodName;

    /**
     * 商品价格
     */
    @ApiModelProperty(required = false, value = "商品价格")
    private Double goodPrice;


    /**
     * 库存
     */
    @ApiModelProperty(required = false, value = "库存")
    private Integer quantity;

}
