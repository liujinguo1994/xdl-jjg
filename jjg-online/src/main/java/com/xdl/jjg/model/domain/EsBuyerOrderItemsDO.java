package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.goods.api.model.domain.EsGoodsSkuDO;
import com.shopx.goods.api.model.domain.EsSpecValuesDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 提供给买家端 的评价信息列表
 * 我的订单 订单商品详情
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsBuyerOrderItemsDO extends Model<EsBuyerOrderItemsDO> {

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
	 * 产品sn
	 */
	private String goodsSn;
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
	 * 商品SKU 信息
	 */
	private EsGoodsSkuDO esGoodsSkuDO;
	/**
	 * 订单创建时间
	 */
	private Long createTime;
	/**
	 * 店铺ID
	 */
	private Long shopId;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 规格列表
	 */
	private List<EsSpecValuesDO> specList;

	/**
	 * 发票类型
	 */
	private Integer receiptType;

	/**
	 *发票状态
	 */
	private Integer receiptState;
	/**
	 * 发票操作
	 */
	private String receiptOperation;

	/** add  添加单品退款功能 start **/
	@ApiModelProperty(value = "订单商品状态")
	private String goodsStatus;

	/**
	 * 配送费用
	 */
	@ApiModelProperty(name = "shipping_price", value = "配送费用", required = false)
	private Double shippingPrice;

	@ApiModelProperty(value = "普通配送费" )
	private Double  commonFreightPrice;

	@ApiModelProperty(value = "生鲜配送费" )
	private Double  freshFreightPrice;
	/** add  添加单品退款功能 end **/

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
