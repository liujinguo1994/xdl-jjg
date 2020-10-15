package com.jjg.shop.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * Form
 * </p>
 *
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsTagsForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签名字
     */
	@ApiModelProperty(value = "标签名字")
	private String tagName;

    /**
     * 关键字
     */
	@ApiModelProperty(value = "关键字")
	private String mark;

	@ApiModelProperty(value = "排序")
	private Integer sort;

}
