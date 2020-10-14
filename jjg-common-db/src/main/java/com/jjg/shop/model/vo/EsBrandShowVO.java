package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-12 13:23:47
 */
@Data
@ApiModel
public class EsBrandShowVO implements Serializable {
	@ApiModelProperty(value = "主键ID")
	private Integer id;

	@ApiModelProperty(value = "品牌展示名称")
	private String name;

	@ApiModelProperty(value = "品牌logo链接")
	private String imgUrl;

	@ApiModelProperty(value = "品牌点击链接")
	private String linkUrl;

	@ApiModelProperty(value = "排序")
	private Integer sort;

}
