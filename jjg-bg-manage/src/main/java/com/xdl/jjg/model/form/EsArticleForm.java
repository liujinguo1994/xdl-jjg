package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@ApiModel
public class EsArticleForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 文章名称
     */
    @ApiModelProperty(value = "文章名称")
    @NotBlank(message = "文章名称不能为空")
	private String articleName;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    @NotNull(message = "分类id不能为空")
	private Long categoryId;

    /**
     * 文章排序
     */
    @ApiModelProperty(value = "文章排序")
	private Integer sort;

    /**
     * 外链url
     */
    @ApiModelProperty(value = "外链url")
	private String outsideUrl;

    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容")
    @NotBlank(message = "文章内容不能为空")
	private String content;
}
