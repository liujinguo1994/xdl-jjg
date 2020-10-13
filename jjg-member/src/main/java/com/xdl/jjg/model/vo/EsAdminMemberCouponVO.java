package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 后台会员优惠券列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsAdminMemberCouponVO implements Serializable {

    /**
     * 会员id
     */
    @ApiModelProperty(required = false,value = "会员id",example = "1")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(required = false,value = "名称")
	private String name;
    /**
     * 手机号
     */
    @ApiModelProperty(required = false,value = "手机号")
	private String mobile;
    /**
     * 已领取待使用数量
     */
    @ApiModelProperty(required = false,value = "已领取待使用数量",example = "1")
	private Integer noUseNum;
    /**
     * 已使用数量
     */
    @ApiModelProperty(required = false,value = "已使用数量",example = "1")
	private Integer hasMadeNum;
    /**
     * 已过期数量
     */
    @ApiModelProperty(required = false,value = "已过期数量",example = "1")
	private Integer expireCouponNum;

}
