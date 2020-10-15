package com.jjg.shop.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * Form
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-18 09:39:18
 */
@Data
@ApiModel
public class EsShopPromiseForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 承诺内容
     */
	@ApiModelProperty(value = "承诺内容")
	@NotBlank(message = "承诺内容 不能为空")
	private String content;

    /**
     * 有效状态
     */
	@ApiModelProperty(value = "有效状态 1 有效 2无效")
	@NotNull
	private Integer state;


}
