package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.shop.model.domain.EsSpecValuesDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单商品明细表-es_order_items
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsOrderItemsDO extends Model<EsOrderItemsDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品ID
     */
	private Long goodsId;
	/**
	 * 产品sn
	 */
	private String goodsSn;
    /**
     * skuID
     */
	private Long skuId;
    /**
     * 数量
     */
	private Integer num;
    /**
     * 已发货数量
     */
	private Integer shipNum;
    /**
     * 订单编号
     */
	private String tradeSn;
    /**
     * 子订单编号
     */
	private String orderSn;
    /**
     * 图片
     */
	private String image;
    /**
     * 商品名称
     */
	private String name;
    /**
     * 优惠后价格
     */
	private Double money;
    /**
     * 分类ID
     */
	private Long categoryId;
    /**
     * 售后状态
     */
	private String state;

	/**
	 * 售后状态
	 */
	private String stateText;
    /**
     * 支付快照id
     */
	private Long snapshotId;
    /**
     * 规格json
     */
	private String specJson;
    /**
     * 促销类型
     */
	private String promotionType;
    /**
     * 活动ID
     */
	private Long promotionId;
    /**
     * 发货单号
     */
	private String shipNo;
    /**
     * 签收人
     */
	private String theSign;
    /**
     * 物流公司ID
     */
	private Long logiId;
    /**
     * 物流公司名称
     */
	private String logiName;
	/**
	 * 单品评价是否完成
	 */
	private String singleCommentStatus;
	/**
	 * 发货状态 0 未发货，1 已发货
	 */
	private Integer hasShip;
	/**
	 * 发票流水号
	 */
	private String invoiceNumber;
	/**
	 * 商品SKU 信息
	 */
	private EsGoodsSkuDO esGoodsSkuDO;
	/**
	 * 商品是否是自提（1 自提 2 物流）
	 */
	private Integer isDelivery;
	/**
	 * 配送方式 notInScope 不在配送范围，express 快递，selfMention 自提
	 */
	private String deliveryMethod;
	/**
	 * 规格列表
	 */
	private List<EsSpecValuesDO> specList;

	/**
	 * 售后单号
	 */
	private Long reFundSn;
	@ApiModelProperty(value = "售后处理状态")
	private String serviceHandleStatus;

	/**
	 * 1 是赠品，2 非赠品
	 */
	@ApiModelProperty(value = "1 是赠品，2 非赠品")
	private Integer isGift = 2;

	@ApiModelProperty(value = "上下架状态 2:下架 其他状态为空")
	private Integer marketEnable;





	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
