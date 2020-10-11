package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 热门关键字
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsHotKeywordVO implements Serializable {

    private static final long serialVersionUID = 5775261063201816413L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",example = "1")
    private Long id;
    /**
     * 文字内容
     */
    @ApiModelProperty(value = "文字内容")
    private String hotName;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序",example = "1")
    private Integer sort;

}
