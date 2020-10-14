package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 好中差评设置
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-29 14:18:12
 */
@Data
@ApiModel
public class EsCommentSortConfigForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    /**
     * 评分最小值
     */
    @ApiModelProperty(value = "评分最小值", example = "1")
    private Double minScore;

    /**
     * 评分最大值
     */
    @ApiModelProperty(value = "评分最大值", example = "1")
    private Double maxScore;

    /**
     * 评论分类类型 0:好评,1:中评.2:差评
     */
    @ApiModelProperty(value = "评论分类类型 (0:好评,1:中评.2:差评)", example = "1")
    private Integer commentType;

    @ApiModelProperty(value = "评论分类名称")
    private String commentSort;

}
