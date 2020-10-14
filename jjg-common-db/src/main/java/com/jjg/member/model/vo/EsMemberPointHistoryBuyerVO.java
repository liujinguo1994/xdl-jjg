package com.jjg.member.model.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 买家端-会员积分明细VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:27
 */
@Data
@Api
@Accessors(chain = true)
public class EsMemberPointHistoryBuyerVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 购物赠送数量
	 */
	@ApiModelProperty(value = "购物赠送数量")
	private Long shoppingNum;

	/**
	 * 评论赠送数量
	 */
	@ApiModelProperty(value = "评论赠送数量")
	private Long commentNum;

	/**
	 * 其他赠送数量
	 */
	@ApiModelProperty(value = "其他赠送数量")
	private Long otherNum;

	/**
	 * 我的积分总数
	 */
	@ApiModelProperty(value = "我的积分总数")
	private Long totalNum;

}
