package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员优惠券管理
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsCouponManageMementVO implements Serializable {

    /**
     * 主键ID
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1")
    private Long id;
    /**
     * 会员ID
     */
    @ApiModelProperty(required = false, value = "会员ID", example = "1")
    private Long memberId;
    /**
     * 优惠券表主键 (es_coupon)
     */
    @ApiModelProperty(required = false, value = "优惠券表主键", example = "1")
    private Long couponId;
    /**
     * 使用时间
     */
    @ApiModelProperty(required = false, value = "使用时间", example = "112111")
    private Long updateTime;
    /**
     * 领取时间
     */
    @ApiModelProperty(required = false, value = "使用时间", example = "112111")
    private Long createTime;
    /**
     * 订单编号
     */
    @ApiModelProperty(required = false, value = "使用时间")
    private String orderSn;
    /**
     * 优惠券名称
     */
    @ApiModelProperty(required = false, value = "优惠券名称")
    private String title;
    /**
     * 优惠券面额
     */
    @ApiModelProperty(required = false, value = "优惠券面额",example = "12.22")
    private Double couponMoney;
    /**
     * 使用起始时间
     */
    @ApiModelProperty(required = false, value = "使用起始时间",example = "122111")
    private Long startTime;
    /**
     * 使用状态 1待使用，2已使用
     */
    @ApiModelProperty(required = false, value = "使用状态 1待使用，2已使用",example = "1")
    private Integer state;
    /**
     * 商家ID,优惠券属于哪个商家
     */
    @ApiModelProperty(required = false, value = "商家ID,优惠券属于哪个商家",example = "1211")
    private Long shopId;
    /**
     * 商家店名
     */
    @ApiModelProperty(required = false, value = "商家店名")
    private String shopName;
    /**
     * 是否过期(1未过期，2已过期)
     */
    @ApiModelProperty(required = false, value = "是否过期(1未过期，2已过期)")
    private Integer overdueState;
    private String stateText;
    private String overdueStateText;
}
