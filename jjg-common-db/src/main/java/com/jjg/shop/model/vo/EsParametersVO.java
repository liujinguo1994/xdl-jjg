package com.jjg.shop.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsParametersVO implements Serializable {
  /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 参数名称
     */
	@ApiModelProperty(value = "参数名称")
	private String paramName;

    /**
     * 参数类型1 输入项   2 选择项
     */
	@ApiModelProperty(value = "参数类型1 输入项   2 选择项")
	private Integer paramType;

    /**
     * 选择值，当参数类型是选择项2时，必填，逗号分隔
     */
	@ApiModelProperty(value = "选择值，当参数类型是选择项2时，必填，逗号分隔")
	private String options;

    /**
     * 是否可索引，0 不显示 1 显示
     */
	@ApiModelProperty(value = "是否可索引，0 不显示 1 显示")
	private Integer isIndex;

    /**
     * 是否必填是  1    否   0
     */
	@ApiModelProperty(value = "是否必填是  1    否   0")
	private Integer required;

    /**
     * 参数分组id
     */

	@ApiModelProperty(value = "参数分组id")
	private Long groupId;

    /**
     * 商品分类id
     */
	@ApiModelProperty(value = "商品分类id")
	private Long categoryId;

    /**
     * 排序
     */
	@ApiModelProperty(value = "排序")
	private Integer sort;
	@ApiModelProperty(value = "参数值")
	private String paramValue;
	private String[] optionList;
}
