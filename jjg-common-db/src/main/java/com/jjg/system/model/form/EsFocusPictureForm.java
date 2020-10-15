package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 轮播图
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsFocusPictureForm implements Serializable {

    private static final long serialVersionUID = 7981058516141512754L;
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", example = "1")
    private Long id;

    /**
     * 图片地址
     */
    @ApiModelProperty(required = true, value = "图片地址")
    @NotBlank(message = "图片地址不能为空")
    private String picUrl;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型")
    private String operationType;

    /**
     * 操作参数
     */
    @ApiModelProperty(value = "操作参数")
    private String operationParam;

    /**
     * 操作地址
     */
    /*@ApiModelProperty(value = "操作地址")
	private String operationUrl;*/
    /**
     * 客户端类型 APP/WAP/PC
     */
    @ApiModelProperty(required = true, value = "客户端类型 APP/WAP/PC")
    @NotBlank(message = "客户端类型不能为空")
    private String clientType;

    /**
     * 背景色
     */
    @ApiModelProperty(value = "背景色")
    private String operationColor;

}
