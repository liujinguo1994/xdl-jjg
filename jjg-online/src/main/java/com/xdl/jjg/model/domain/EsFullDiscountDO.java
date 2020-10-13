package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 满减满赠-es_full_discount
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsFullDiscountDO extends Model<EsFullDiscountDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
	private Long id;

    /**
     * 优惠门槛金额
     */
	private Double fullMoney;

    /**
     * 减现金
     */
	private Double minusValue;

    /**
     * 活动是否减现金
     */
	private Integer isFullMinus;

    /**
     * 是否免邮
     */
	private Integer isFreeShip;

    /**
     * 是否有赠品
     */
	private Integer isSendGift;

    /**
     * 是否赠优惠券
     */
	private Integer isSendBonus;

    /**
     * 赠品id(支持多赠品)
     */
	private Long giftId;

    /**
     * 优惠券id
     */
	private Long bonusId;

    /**
     * 是否打折
     */
	private Integer isDiscount;

    /**
     * 折扣
     */
	private Double discountValue;

    /**
     * 活动开始时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 活动结束时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;


	private Long startTime;

	private Long endTime;
    /**
     * 活动标题
     */
	private String title;

    /**
     * 商品参与方式，全部商品：1，部分商品：2
     */
	private Integer rangeType;

    /**
     * 是否停用
     */
	private Integer isDel;

    /**
     * 活动说明
     */
	private String description;

    /**
     * 商家id
     */
	private Long shopId;

    /**
     * 商家名称
     */
	private String shopName;

	private String  statusText;

	private String status;

    @ApiModelProperty(value = "赠品信息")
    private EsFullDiscountGiftDO esFullDiscountGiftDO;

    @ApiModelProperty(value = "赠品优惠券信息")
    private EsCouponDO esCouponCO;

	private List<ESGoodsSelectDO> goodsList;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
