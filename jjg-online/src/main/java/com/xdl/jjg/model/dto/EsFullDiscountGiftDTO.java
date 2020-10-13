package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.shopx.goods.api.model.domain.dto.EsGoodsSkuDTO;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsFullDiscountGiftDTO implements Serializable {

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
     * 店铺id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

	/**
	 * 商品id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long goodsId;

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


	private List<EsGoodsSkuDTO> skuList;
	protected Serializable pkVal() {
		return this.id;
	}

}
