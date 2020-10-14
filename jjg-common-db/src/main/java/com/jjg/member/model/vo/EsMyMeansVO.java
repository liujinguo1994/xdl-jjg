package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *我的资产
 * @author xl 236154186@qq.com
 * @since 2019-12-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMyMeansVO implements Serializable {

    /**
     * 账户余额
     */
    @ApiModelProperty(required = false,value = "账户余额",example = "1")
    private Double balanceAccount;
    /**
     * 优惠券数量
     */
    @ApiModelProperty(required = false,value = "优惠券数量")
    private Integer couponNum;
    /**
     * 积分数量
     */
    @ApiModelProperty(required = false,value = "积分数量")
    private Long pointNum;
    /**
     * 收藏商品数量
     */
    @ApiModelProperty(required = false,value = "收藏商品数量")
    private Integer collectionGoodNum;
    /**
     * 收藏店铺数量
     */
    @ApiModelProperty(required = false,value = "收藏店铺数量")
    private Integer collectionShopNum;

}
