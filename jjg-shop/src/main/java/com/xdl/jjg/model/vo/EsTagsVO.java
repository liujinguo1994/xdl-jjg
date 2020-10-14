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
public class EsTagsVO implements Serializable {
    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 标签名字
     */
	@ApiModelProperty(value = "标签名字")
	private String tagName;

    /**
     * 所属卖家
     */
	@ApiModelProperty(value = "所属卖家")
	private Long shopId;

    /**
     * 关键字
     */
	@ApiModelProperty(value = "关键字")
	private String mark;

	@ApiModelProperty(value = "排序")
	private Integer sort;
}
