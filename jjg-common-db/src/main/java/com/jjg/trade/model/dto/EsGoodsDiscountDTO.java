package com.jjg.member.model.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class EsGoodsDiscountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 活动开始时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long startTime;

    /**
     * 活动结束时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
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
    @TableLogic
	private Integer isDel;

    /**
     * 描述
     */
	private String remark;

    /**
     * 商家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 修改时间
     */
	private Long updateTime;

	/**
	 * 商品集合
	 */
	private List<EsPromotionGoodsDTO> goodsList;

	protected Serializable pkVal() {
		return this.id;
	}

}
