package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品评论标签内容
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsCommentLabelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1")
    private Long id;
    /**
     * 评论标签内容
     */
    @ApiModelProperty(required = false, value = "评论标签内容")
    private String commentLabel;
    /**
     * 创建时间
     */
    @ApiModelProperty(required = false,value = "创建时间",example = "1559303049597")
    private Long createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(required = false,value = "修改时间",example = "1559303049597")
    private Long updateTime;
    /**
     * 标签类型 0：商品，1物流，2服务
     */
    @ApiModelProperty(required = false,value = "标签类型",example = "0")
    private Integer type;

}
