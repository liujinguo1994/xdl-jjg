package com.xdl.jjg.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 *订单评论
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Api
public class OrderCommentVO extends Model<OrderCommentVO> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单主键
     */
	@ApiModelProperty(value = "订单主键")
	private Long id;
    /**
     * 父订单编号
     */
	@ApiModelProperty(value = "父订单编号")
	private String tradeSn;
    /**
     * 子订单订单编号
     */
	@ApiModelProperty(value = "子订单订单编号")
	private String orderSn;
    /**
     * 店铺ID
     */
	@ApiModelProperty(value = "店铺ID")
	private Long shopId;
    /**
     * 店铺名称
     */
	@ApiModelProperty(value = "店铺名称")
	private String shopName;
    /**
     * 会员ID
     */
	@ApiModelProperty(value = "会员ID")
	private Long memberId;
    /**
     * 买家账号
     */
	@ApiModelProperty(value = "买家账号")
	private String memberName;
    /**
     * 订单状态
     */
	@ApiModelProperty(value = "订单状态")
	private String orderState;

    /**
     * 评论是否完成
     */
	@ApiModelProperty(value = "评论是否完成")
	private String commentStatus;

    /**
     * 商品数量
     */
	@ApiModelProperty(value = "商品数量")
	private Integer goodsNum;
	/**
	 * 订单创建时间
	 */
	@ApiModelProperty(value = "订单创建时间")
	private Long createTime;

	/**
     *  订单中的商品信息
	 */
	@ApiModelProperty(value = "订单中的商品信息")
	private List<EsBuyerOrderItemsVO> esBuyerOrderItemsVO;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
