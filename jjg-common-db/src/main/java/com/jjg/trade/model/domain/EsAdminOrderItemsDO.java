package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.shop.model.domain.EsSpecValuesDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 提供给系统后台 发票信息
 *
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
public class EsAdminOrderItemsDO extends Model<EsAdminOrderItemsDO> {

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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
