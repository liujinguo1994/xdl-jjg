package com.jjg.trade.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class EsHalfPriceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 起始时间
     */
	private Long createTime;

    /**
     * 结束时间
     */
	private Long updateTime;

    /**
     * 活动标题
     */
	private String title;

    /**
     * 商品参与方式1全部商品：2，部分商品
     */
	private Integer rangeType;

    /**
     * 是否停用 0.没有停用 1.停用
     */
	private Integer isDel;

    /**
     * 活动说明
     */
	private String description;

	/**
	 * 活动开始时间
	 */
	private Long startTime;

	/**
	 * 活动结束时间
	 */
	private Long endTime;

	/**
	 * 商品集合
	 */
	private List<EsPromotionGoodsDTO> goodsList;

    /**
     * 商家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

	protected Serializable pkVal() {
		return this.id;
	}

}
