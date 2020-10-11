package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 商品标签
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-27 14:57:57
 */
@Data
@ApiModel
public class EsAdminTagsForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签名字
     */
	@ApiModelProperty(value = "标签名字")
	@NotBlank(message = "标签名字不能为空")
	private String tagName;

}
