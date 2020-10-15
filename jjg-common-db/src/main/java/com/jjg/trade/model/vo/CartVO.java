package com.jjg.trade.model.vo;

import com.shopx.common.util.BeanUtil;
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
public class CartVO implements Serializable {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "店铺ID")
	private Long shopId;

	@ApiModelProperty(value = "店铺名称")
	private String shopName;

	@ApiModelProperty(value = "购物车商品")
    private List<CartItemsVO> cartItemsList;

	@ApiModelProperty(value = "购物车页展示时，店铺内的商品是否全选状态.1为店铺商品全选状态, 0位非全选")
	private Integer checked;

	@ApiModelProperty(value = "促销活动集合（包含商品")
	private List<GroupPromotionVO>  promotionList;

	@ApiModelProperty(value = "活动集合")
	private List<PreferentialMessageVO>  preferentialList;

	@ApiModelProperty(value = "已使用的优惠券列表")
	private List<OrderCouponVO> couponList;

	@ApiModelProperty(value = "赠品列表" )
	private List<EsFullDiscountGiftVO> giftList;

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
	private String shippingTypeName = "快递";

	/**
	 * 构造器
	 * @param shopId		店铺ID
	 * @param shopName	店铺姓名
	 */
	public CartVO(Long shopId, String shopName) {
		this.setShopId(shopId);
		this.setShopName(shopName);
		this.setCartItemsList(new ArrayList<CartItemsVO>());
		this.notFreshFreightPrice=0.0;
		this.freshFreightPrice=0.0;
	}
    // V_2 start
    // 购物车商品项转换

	@ApiModelProperty(value = "店铺发行的优惠券列表")
	private List<EsCouponVO> esCouponVO;
	public CartVO(CartVO cartVO){
        List<PreferentialMessageVO> preferentialList = cartVO.getPreferentialList();
        List<CartItemsVO> cartItemsList = new ArrayList<>();
        preferentialList.forEach(preferentialMessageVO -> {
            List<CartItemsVO> skuLists = preferentialMessageVO.getSkuList();
            cartItemsList.addAll(skuLists);
        });
        BeanUtil.copyProperties(cartVO,this);
        this.setCartItemsList(cartItemsList);
	}
    // V_2 end

	public CartVO() {

	}
}
