package com.jjg.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.shopx.common.util.MathUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单价格信息
 * @since v6.2
 * @author kingapex
 * @version v1.0
 * @created 2017年08月17日
 */
@Data
@ApiModel(value="PriceDetailVO", description = "价格明细")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PriceDetailVO implements Serializable{


	private static final long serialVersionUID = -960537582096338500L;

	@ApiModelProperty(value = "总价")
	private Double  totalPrice;

	@ApiModelProperty(value = "优惠前总价")
	private Double  beforeCompanyPrice;

	@ApiModelProperty(value = "享受公司折扣后的总价")
	private Double  afterCompanyPrice;

	@ApiModelProperty(value = "生鲜总价")
	private Double  xianTotalPrice;

	@ApiModelProperty(value = "商品价格" )
	private Double  goodsPrice;

	@ApiModelProperty(value = "余额" )
	private Double balance;


	@ApiModelProperty(value = "配送费" )
	private Double  freightPrice;

	@ApiModelProperty(value = "普通配送费" )
	private Double  commonFreightPrice;

	@ApiModelProperty(value = "生鲜配送费" )
	private Double  freshFreightPrice;

	@ApiModelProperty(value = "需要第三方支付的金额" )
	private Double  needPayMoney;

	/**
	 * 此金额 = 各种优惠工具的优惠金额 + 优惠券优惠的金额
	 */
	@ApiModelProperty(value = "优惠金额" )
	private Double  discountPrice;

	/**
	 * 结算时商品数量
	 */
	@ApiModelProperty(value = "结算时商品数量" )
	private Integer goodsNum;

	/**
	 * 2为免运费
	 */
	@ApiModelProperty(value = "是否免运费,1为不免运费 ，2为免运费" )
	private Integer isFreeFreight;

	@ApiModelProperty(value = "积分" )
	private Integer exchangePoint;

	@ApiModelProperty(value = "公司折扣金额" )
	private Double  companyDiscountPrice;

	/**
	 * 构造器，初始化默认值
	 */
	public PriceDetailVO() {
        this.goodsPrice = 0.0;
        this.freightPrice = 0.0;
        this.totalPrice = 0.0;
        this.beforeCompanyPrice= 0.0;
        this.afterCompanyPrice= 0.0;
        this.discountPrice = 0.0;
        this.companyDiscountPrice= 0.0;
        this.exchangePoint = 0;
        this.isFreeFreight = 1;
        this.balance = 0.0;
        this.needPayMoney = 0.0;
        this.xianTotalPrice=0.0;
        this.commonFreightPrice=0.0;
        this.freshFreightPrice=0.0;
	}

	/**
	 * 价格累加运算
	 *
	 * @param price 购物车价格
	 * @return
	 */
	public PriceDetailVO plus(PriceDetailVO price) {

		this.setTotalPrice(MathUtil.add(totalPrice, price.getTotalPrice()));
		this.setGoodsPrice(MathUtil.add(goodsPrice, price.getGoodsPrice()));
		this.setFreightPrice(MathUtil.add(this.freightPrice, price.getFreightPrice()));
		this.setDiscountPrice(MathUtil.add(this.discountPrice, price.getDiscountPrice()));
		this.setCompanyDiscountPrice(MathUtil.add(this.companyDiscountPrice, price.getCompanyDiscountPrice()));
		this.setAfterCompanyPrice(MathUtil.add(this.afterCompanyPrice, price.getAfterCompanyPrice()));
		this.setExchangePoint(this.exchangePoint + price.getExchangePoint());
		this.setXianTotalPrice(MathUtil.add(this.xianTotalPrice,price.getXianTotalPrice()));
		this.setCommonFreightPrice( MathUtil.add(this.commonFreightPrice,price.getCommonFreightPrice()));
		this.setFreshFreightPrice(MathUtil.add(this.freshFreightPrice,price.getFreshFreightPrice()));
		// 需要第三方支付的金额，
		this.setNeedPayMoney(MathUtil.subtract(this.getTotalPrice(),price.getBalance()));
		return price;
	}

	/**
	 * 当前店铺总价计算
	 */
	public void countPrice() {
		// 购物车内当前商家的商品原价总计
		Double goodsPrice = this.getGoodsPrice();

		// 购物车内当前商家的促销优惠金额总计（不含优惠券） 卖家端设置的是百分比的 所以需要除100
		Double discountPrice = this.getDiscountPrice();

		// 总公司优惠金额
		Double companyDiscountPrice = this.getCompanyDiscountPrice();

		// 购物车内当前商家的配送金额总计
		Double freightPrice = this.getFreightPrice();

		// 购物车内当前商家的应付金额总计
		// 运算过程=商品原价总计 - 优惠金额总计 + 配送费用
		Double subtract = MathUtil.subtract(goodsPrice, discountPrice);
		// 避免为负数
		Double v = subtract >= 0.0 ? subtract : 0.0;

		Double beforeCompanyPrice = MathUtil.add(v,freightPrice);


		// 参与公司优惠后的金额
		Double totalPrice = MathUtil.subtract(beforeCompanyPrice, companyDiscountPrice);
		Double afterCompanyPrice = MathUtil.subtract(goodsPrice, companyDiscountPrice);

		// 防止金额为负数
		if (totalPrice <= 0) {
			totalPrice = 0d;
			discountPrice = 0d;
			afterCompanyPrice = 0d;
		}
		this.setTotalPrice(totalPrice);
		this.setNeedPayMoney(MathUtil.subtract(totalPrice,this.getBalance()));
		this.setDiscountPrice(discountPrice);
		this.setAfterCompanyPrice(afterCompanyPrice);
		this.setBeforeCompanyPrice(beforeCompanyPrice);

	}
    /**
     * 当前店铺总价计算
     */
    public void countPrice_coupon() {
        // 购物车内当前商家的商品原价总计
        Double goodsPrice = this.getGoodsPrice();

        // 购物车内当前商家的促销优惠金额总计（不含优惠券） 卖家端设置的是百分比的 所以需要除100
        Double discountPrice = this.getDiscountPrice();

        // 总公司优惠金额
        Double companyDiscountPrice = this.getCompanyDiscountPrice();

        // 购物车内当前商家的配送金额总计
        Double freightPrice = this.getFreightPrice();

        // 购物车内当前商家的应付金额总计
        // 运算过程=商品原价总计 - 优惠金额总计
        Double beforeCompanyPrice = MathUtil.subtract(goodsPrice, discountPrice);


        // 参与公司优惠后的金额
        Double totalPrice = MathUtil.subtract(beforeCompanyPrice, companyDiscountPrice);
        Double afterCompanyPrice = MathUtil.subtract(goodsPrice, companyDiscountPrice);

        // 防止金额为负数
        if (totalPrice <= 0) {
            totalPrice = 0d;
            discountPrice = 0d;
            afterCompanyPrice = 0d;
        }
        this.setTotalPrice(totalPrice);
        this.setNeedPayMoney(MathUtil.subtract(totalPrice,this.getBalance()));
        this.setDiscountPrice(discountPrice);
        this.setAfterCompanyPrice(afterCompanyPrice);
        this.setBeforeCompanyPrice(beforeCompanyPrice);

    }
}
