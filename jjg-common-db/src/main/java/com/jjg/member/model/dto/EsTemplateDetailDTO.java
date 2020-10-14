package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺模板详情--es_template_detail
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsTemplateDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 模板类型
     */
	private Integer type;

    /**
     * 模板名称
     */
	private String name;

    /**
     * 位置
     */
	private Integer location;

    /**
     * 修改时间
     */
	private Long updateTime;

    /**
     * 模板规格
     */
	private Integer tempSize;

    /**
     * 应用商品
     */
	private String hotSellGoods;

    /**
     * 应用分类
     */
	private String categoryForTems;

    /**
     * 客户端类型
     */
	private Integer clientType;

	protected Serializable pkVal() {
		return this.id;
	}

}
