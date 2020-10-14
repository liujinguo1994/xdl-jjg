package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品评论标签关联分类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsCommentCategoryCopyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1")
    private Long id;
    /**
     * 评论标签id
     */
    @ApiModelProperty(required = false, value = "评论标签id", example = "1")
    private Long labelId;
    /**
     * 商品分类id
     */
    @ApiModelProperty(required = false, value = "商品分类id", example = "1")
    private Long categoryId;
    /**
     * 标记是否被绑定 0绑定，1未绑定
     */
    @ApiModelProperty(required = false, value = "标记是否被绑定",example ="1")
    private Integer unbind;
    /**
     * 评论标签内容
     */
    @ApiModelProperty(required = false, value = "评论标签内容")
    private String commentLabel;
    /**
     * 标签类型 0：商品，1物流，2服务
     */
    @ApiModelProperty(required = false,value = "标签类型",example = "0")
    private Integer type;

}
