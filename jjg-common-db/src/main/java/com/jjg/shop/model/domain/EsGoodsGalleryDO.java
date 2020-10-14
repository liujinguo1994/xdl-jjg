package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
public class EsGoodsGalleryDO implements  Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * skuID
     */
	private Long skuId;
    /**
     * 缩略图路径
     */
	private String image;
    /**
     * 排序
     */
	private Integer sort;
	private String albumNo;
	protected Serializable pkVal() {
		return this.id;
	}

}
