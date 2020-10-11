package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 *    热门关键字
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsHotKeywordForm implements Serializable {

    private static final long serialVersionUID = -5572328776172211369L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",example = "1")
	private Long id;
    /**
     * 文字内容
     */
    @ApiModelProperty(required = true , value = "文字内容")
    @NotBlank(message = "文字内容不能为空")
	private String hotName;
    /**
     * 排序
     */
    @ApiModelProperty(required = true , value = "排序",example = "1")
    @NotNull(message = "排序不能为空")
	private Integer sort;

}
