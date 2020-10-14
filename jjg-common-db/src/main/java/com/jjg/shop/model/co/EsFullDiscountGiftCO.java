package com.xdl.jjg.model.co;


import com.shopx.goods.api.model.domain.EsSellerGoodsSkuDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsFullDiscountGiftCO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 赠品名称
     */
	private String giftName;

    /**
     * 赠品金额
     */
	private Double giftMoney;

    /**
     * 赠品图片
     */
	private String giftImg;

    /**
     * 赠品类型
     */
	private Integer giftType;

    /**
     * 可用库存
     */
	private Integer enableStore;

    /**
     * 档案SKUid
     */
	private Long skuiId;

	/**
	 * 商品id
	 */
	private Long goodsId;

    /**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 实际库存
	 */
	private Integer quantity;

	/**
	 * 可用库存=真实库存+虚拟库存-冻结库存
	 */
	private Integer enableQuantity;

	/**
	 * 虚拟库存
	 */
	private Integer xnQuantity;

	private Long createTime;

	private Long updateTime;

	private List<EsSellerGoodsSkuDO> skuList;

}
