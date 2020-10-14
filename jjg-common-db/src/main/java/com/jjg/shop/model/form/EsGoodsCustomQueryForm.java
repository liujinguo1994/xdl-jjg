package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Form
 * </p>
 *
 * @author wangaf 826988665@qq
 * @since 2019-06-025 17:30:01
 */
@Data
@ApiModel
public class EsGoodsCustomQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 输入值
     */
	@ApiModelProperty(value = "输入值")
	private String keyword;

	/**
	 * 分类路径
	 */
	@ApiModelProperty(value = "分类路径")
	private String categoryPath;
	/**
     * 店铺自定义分类ID
	 */
	@ApiModelProperty(value = "分组状态 0 全部 1 已分组 2 未分组")
	private String customState;
}
