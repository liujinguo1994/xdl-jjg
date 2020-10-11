package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 *  地区
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsRegionsForm implements Serializable {


    private static final long serialVersionUID = 1691955554297252619L;
    /**
     * id
     */
    @ApiModelProperty(value = "id",example = "1")
    private Long id;

    /**
     * 父地区id
     */
	@ApiModelProperty(value = "父地区id",example = "1")
	private Long parentId;

    /**
     * 名称
     */
    @ApiModelProperty(required = true, value = "名称")
    @NotBlank(message = "名称不能为空")
	private String localName;

    /**
     * 邮编
     */
    @ApiModelProperty(value = "邮编",example = "1")
	private String zipcode;

}
