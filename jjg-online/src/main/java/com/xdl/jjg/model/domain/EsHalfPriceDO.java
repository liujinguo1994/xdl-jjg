package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsHalfPriceDO extends Model<EsHalfPriceDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 起始时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 结束时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
     * 商家id
     */
	private Long shopId;


	private String  statusText;

	private String status;

	private List<ESGoodsSkuSelectDO> goodsList;
	private Long startTime;

	private Long endTime;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
