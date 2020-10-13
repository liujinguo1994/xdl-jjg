package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 组合商品活动模型
 * @author Snow
 * @since v6.4
 * @version v1.0
 * 2017年08月23日16:06:48
 */
@SuppressWarnings("AlibabaPojoMustOverrideToString")
@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GroupPromotionVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3188594102818543304L;

	/**
	 * 促销活动工具类型 存储PromotionTypeEnum.XXX.getType();
	 */
	@ApiModelProperty(value = "促销活动工具类型")
	private String promotionType;

	/**
	 * 根据以上的活动工具类型 存储对应的Vo<br>
	 * 例如：上面的类型为groupbuy，那么此处的则为GroupbuyVo
	 */
	@ApiModelProperty(value = "活动详情")
	private Object activityDetail;

	@ApiModelProperty(value = "商品集合")
	private List<CartItemsVO> skuList;

	/**
	 * 商家价格小计 = 商品集合中小计的总和。
	 */
	@ApiModelProperty(value = "商品价格小计")
	private Double subtotal;


	@ApiModelProperty(value = "是否是组合活动,1为是组合活动，2为单品活动")
	private Integer isGroup;

	@ApiModelProperty(value = "差额")
	private Double spreadPrice;

	@ApiModelProperty(value = "优惠金额")
	private Double discountPrice;

	/**
	 * 购物车页-满优惠活动是否选中状态
	 * 1为选中
	 */
	@ApiModelProperty(value = "活动是否选中.1为选中,0未选中")
	private Integer checked;

	public GroupPromotionVO(){
		this.subtotal = 0.0;
	}

}
