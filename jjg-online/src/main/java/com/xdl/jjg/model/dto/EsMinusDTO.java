package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMinusDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 单品立减金额
     */
	private Double singleReductionValue;
    /**
     * 起始时间
     */
	private Long createTime;
    /**
     * 结束时间
     */
	private Long updateTime;
    /**
     * 单品立减活动标题
     */
	private String title;
    /**
     * 商品参与方式（1，全部商品 2，部分商品）
     */
	private Integer rangeType;
    /**
     * 是否停用
     */
	private Integer isDel;
    /**
     * 描述
     */
	private String remark;
    /**
     * 商家id
     */
	private Long shopId;

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

	protected Serializable pkVal() {
		return this.id;
	}

}
