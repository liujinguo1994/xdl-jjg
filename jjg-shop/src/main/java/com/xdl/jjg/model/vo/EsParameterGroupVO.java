package com.jjg.member.model.vo;

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
public class EsParameterGroupVO implements Serializable {
    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 参数组名称
     */
	@ApiModelProperty(value = "参数组名称")
	private String groupName;

    /**
     * 关联分类id
     */
	@ApiModelProperty(value = "关联分类id")
	private Long categoryId;

    /**
     * 排序
     */
	@ApiModelProperty(value = "排序")
	private Integer sort;

}
