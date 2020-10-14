package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.goods.api.model.domain.vo.EsGoodsSkuVO;
import com.shopx.goods.api.model.domain.vo.EsSpecValuesVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsOrderItemsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
	private Long goodsId;
    /**
     * skuID
     */
    @ApiModelProperty(value = "skuID")
	private Long skuId;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
	private Integer num;
    /**
     * 已发货数量
     */
    @ApiModelProperty(value = "已发货数量")
	private Integer shipNum;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
	private String tradeSn;
    /**
     * 子订单编号
     */
    @ApiModelProperty(value = "子订单编号")
	private String orderSn;
    /**
     * 产品sn
     */
    @ApiModelProperty(value = " 产品sn")
    private String goodsSn;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
	private String image;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
	private String name;
    /**
     * 优惠后价格
     */
    @ApiModelProperty(value = "优惠后价格")
	private Double money;
    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
	private Long categoryId;
    /**
     * 售后状态
     */
    @ApiModelProperty(value = "售后状态")
	private String state;
    /**
     * 售后状态
     */
    @ApiModelProperty(value = "售后状态文本")
    private String stateText;
    /**
     * 支付快照id
     */
    @ApiModelProperty(value = "支付快照id")
	private Long snapshotId;
    /**
     * 规格json
     */
    @ApiModelProperty(value = "规格json")
	private String specJson;
    /**
     * 促销类型
     */
    @ApiModelProperty(value = "促销类型")
	private String promotionType;
    /**
     * 活动ID
     */
    @ApiModelProperty(value = "活动ID")
	private Long promotionId;
    /**
     * 发货单号
     */
    @ApiModelProperty(value = "发货单号")
	private String shipNo;
    /**
     * 签收人
     */
    @ApiModelProperty(value = "签收人")
	private String theSign;
    /**
     * 物流公司ID
     */
    @ApiModelProperty(value = "物流公司ID")
	private Long logiId;
    /**
     * 物流公司名称
     */
    @ApiModelProperty(value = "物流公司名称")
	private String logiName;
	/**
	 * 单品评价是否完成
	 */
    @ApiModelProperty(value = "单品评价是否完成")
	private String singleCommentStatus;
    /**
     * 发货状态 0 未发货，1 已发货
     */
    @ApiModelProperty(value = " 发货状态 0 未发货，1 已发货")
    private Integer hasShip;

    /**
     * 发票流水号
     */
    @ApiModelProperty(value = "发票流水号")
    private String invoiceNumber;

	/**
	 * 规格列表
	 */
    @ApiModelProperty(value = "规格列表")
	private List<EsSpecValuesVO> specList;
	/**
	 * 商品SKU 信息
	 */
    @ApiModelProperty(value = "商品SKU 信息")
	private EsGoodsSkuVO esGoodsSkuVO;

    /**
     * 商品是否是自提（1 自提 2 物流）
     */
    @ApiModelProperty(value = "商品是否是自提（1 自提 2 物流）")
    private Integer isDelivery;

    @ApiModelProperty(value = "配送方式 notInScope 不在配送范围，express 快递，selfMention 自提")
    private String deliveryMethod;


    protected Serializable pkVal() {
		return this.id;
	}

}
