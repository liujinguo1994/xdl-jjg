package com.jjg.trade.model.dto;


import com.jjg.trade.model.domain.EsFullDiscountGiftDO;
import com.jjg.trade.model.vo.EsCouponVO;
import com.jjg.trade.model.vo.EsFreightTemplateDetailVO;
import com.jjg.trade.model.vo.OrderCouponVO;
import com.jjg.trade.model.vo.PriceDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车VO
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel(description = "购物车")
public class CartDTO implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "店铺ID")
	private Long shopId;

	@ApiModelProperty(value = "店铺名称")
	private String shopName;

	@ApiModelProperty(value = "购物车商品")
    private List<CartItemsDTO> cartItemsList;

	@ApiModelProperty(value = "购物车页展示时，店铺内的商品是否全选状态.1为店铺商品全选状态, 0位非全选")
	private Integer checked;

	@ApiModelProperty(value = "已使用的优惠券列表")
	private List<OrderCouponVO> couponList;

	@ApiModelProperty(value = "赠品列表" )
	private List<EsFullDiscountGiftDO> giftList;

	@ApiModelProperty(value = "赠送优惠券列表")
	private List<EsCouponVO> giftCouponList;

	@ApiModelProperty(value = "赠送积分")
	private Integer giftPoint;
	@ApiModelProperty(value = "购物车价格")
	private PriceDetailVO price;

	@ApiModelProperty(value = "购物车重量" )
	private Double weight;

	/**
	 * 购物车与运费详情VO的map映射
	 * key为skuId value 为模版
	 */
	private Map<Long, EsFreightTemplateDetailVO> shipTemplateChildMap;

	@ApiModelProperty(value = "非生鲜运费")
	private Double notFreshFreightPrice;

	@ApiModelProperty(value = "生鲜运费")
	private Double freshFreightPrice;

	@ApiModelProperty(value = "选中的配送方式名称" )
	private String shippingTypeName;
	/**
	 * 构造器
	 * @param shopId		店铺ID
	 * @param sellerName	店铺姓名
	 */
//	public CartDTO(Long shopId, String sellerName) {
//		this.setShopId(shopId);
//		this.setShopName(sellerName);
//		this.setCartItemsList(new ArrayList<CartItemsDTO>());
//		this.setChecked(1);
//	}

	/**
	 * 在构造器中初始化属主、产品列表、促销列表及优惠卷列表
	 */
	public CartDTO(Long shopId, String shopName){

		this.shopId = shopId;
		this.shopName = shopName;
		price = new PriceDetailVO();
		cartItemsList = new ArrayList<CartItemsDTO>();
		couponList = new ArrayList<OrderCouponVO>();
		giftCouponList = new ArrayList<EsCouponVO>();
		giftList = new ArrayList<EsFullDiscountGiftDO>();
	}


	public CartDTO() {

	}
}
