package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员商品收藏
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberGoodsVO implements Serializable {
    /**
     * 商品ID
     */
    @ApiModelProperty(required = false,value = "商品ID")
    private Long id;
    /**
     * 商品名称
     */
    @ApiModelProperty(required = false,value = "商品名称")
    private String goodsName;
    /**
     * 商品价格
     */
    @ApiModelProperty(required = false,value = "商品价格")
    private Double money;
    /**
     * 销量
     */
    @ApiModelProperty(required = false,value = "销量")
    private Integer buyCount;
    /**
     * 图片
     */
    @ApiModelProperty(required = false,value = "图片")
    private String original;
}
