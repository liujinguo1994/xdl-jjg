package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品折扣活动表
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-28 14:26:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsDiscountDO extends Model<EsGoodsDiscountDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 活动开始时间
     */
	private Long startTime;

    /**
     * 活动结束时间
     */
	private Long endTime;

    /**
     * 折扣
     */
	private Double discount;

    /**
     * 活动标题
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
     * 创建时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 修改时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;
	private String  statusText;

	private String status;

	private List<ESGoodsSkuSelectDO> goodsList;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
