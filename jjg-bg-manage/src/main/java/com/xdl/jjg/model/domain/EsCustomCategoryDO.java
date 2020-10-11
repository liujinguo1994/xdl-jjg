package com.xdl.jjg.model.domain;

import com.xdl.jjg.model.vo.EsFindGoodsVO;
import com.xdl.jjg.model.vo.EsOftenGoodsVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自定义分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Data
public class EsCustomCategoryDO implements Serializable {
	private static final long serialVersionUID = -4046391893811616339L;
	/**
     * 主键ID
     */
	private Long id;

    /**
     * 自定义分类名称
     */
	private String categoryName;

    /**
     * 所属专区
     */
	private Long zoneId;

	/**
	 * 专区名称
	 */
	private String zoneName;

	/**
	 * 发现好货
	 */
	private List<EsFindGoodsVO> findGoodsVOS;

	/**
	 * 常卖清单
	 */
	private List<EsOftenGoodsVO> oftenGoodsVOS;
}
