package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 排行商品
 * </p>
 *
 * @author rm 2817512105@qq.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSalesRankingGoodsDO implements  Serializable {

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品名称
     */
	private String goodsName;
	/**
	 * 购买数量
	 */
	private Integer buyCount;
	/**
	 * 原图路径
	 */
	private String original;
    /**
     * 商品价格
     */
	private Double money;
}
