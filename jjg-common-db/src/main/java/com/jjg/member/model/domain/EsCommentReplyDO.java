package com.jjg.member.model.domain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 评论回复(店家回复)
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@Api
public class EsCommentReplyDO implements Serializable {


    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
	private Long id;
    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
	private Long commentId;
    /**
     * 回复父id
     */
    @ApiModelProperty(value = "回复父id")
	private Long parentId;
    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
	private String content;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
	private Long createTime;
    /**
     * 商家或者买家
     */
    @ApiModelProperty(value = "商家或者买家")
	private String role;
    /**
     * 父子路径0|10|
     */
    @ApiModelProperty(value = "父子路径0|10|")
	private String path;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "主键id")
    private Long shopId;


}
