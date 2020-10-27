package com.jjg.trade.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.shop.model.vo.EsSpecValuesVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提供给买家端 的评价信息列表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsBuyerOrderItemsVO extends Model<EsBuyerOrderItemsVO> {

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
     * 图片
     */
	@ApiModelProperty(value = "图片")
	private String image;
    /**
     * 商品名称
     */
	@ApiModelProperty(value = "主键ID")
	private String name;

	/**
	 * 产品sn
	 */
	@ApiModelProperty(value = " 产品sn")
	private String goodsSn;
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
	 * 商品SKU 信息
	 */
	@ApiModelProperty(value = "商品SKU 信息")
	private EsGoodsSkuDO esGoodsSkuDO;
	/**
     * 订单创建时间
	 */
	@ApiModelProperty(value = "订单创建时间")
	private Long createTime;
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
	 * 发票操作
	 */
	private String receiptOperation;
	/**
	 *发票状态
	 */
	@ApiModelProperty(value = "发票状态2 未开票 开具发票，1 已开票 查看发票")
	private Integer receiptState;

    /**
     * 发票类型
     */
    @ApiModelProperty(value = "1，专票2普票3专票+普票")
    private Integer receiptType;

	/**
	 * 发票历史信息
	 */
	@ApiModelProperty(value = "发票历史信息")
	private EsMemberReceiptHistoryVO receiptHistoryVO;

	/**
	 * 售后单号
	 */
	private Long reFundSn;
	@ApiModelProperty(value = "售后处理状态")
	private String serviceHandleStatus;
	/**
     * 规格列表
	 */
	@ApiModelProperty(value = "规格列表")
	private List<EsSpecValuesVO> specList;

	@ApiModelProperty(value = "商品评论信息")
	private EsCommentInfoVO esCommentInfoVO;

	@ApiModelProperty(value = "上下架状态 2:下架 其他状态为空")
	private Integer marketEnable;

//	@ApiModelProperty(value = "评论标签")
//	private EsCommentCategoryClassifyCopyVO commentCategoryClassifyVO;

	@ApiModelProperty(value = "评论标签")
	private Map<String, Object> commentCategoryClassifyVO;

	/**
	 * 1 是赠品，2 非赠品
	 */
	private Integer isGift;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
