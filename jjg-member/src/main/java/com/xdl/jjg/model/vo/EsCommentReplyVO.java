package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 评论回复(店家回复)
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-24
 */
@Data
@ApiModel
public class EsCommentReplyVO implements Serializable {


    /**
     * 主键
     */
    @ApiModelProperty(required = false,value = "主键")
	private Long id;
    /**
     * 评论id
     */
    @ApiModelProperty(required = false,value = "评论id")
	private Long commentId;
    /**
     * 回复父id
     */
    @ApiModelProperty(required = false,value = "回复父id")
	private Long parentId;
    /**
     * 回复内容
     */
    @ApiModelProperty(required = false,value = "回复内容")
	private String content;
    /**
     * 创建时间
     */
    @ApiModelProperty(required = false,value = "创建时间")
	private Long createTime;
    /**
     * 商家或者买家
     */
    @ApiModelProperty(required = false,value = "商家或者买家")
	private String role;
    /**
     * 父子路径0|10|
     */
    @ApiModelProperty(required = false,value = "父子路径0|10|")
	private String path;

    /**
     * 店铺id
     */
    @ApiModelProperty(required = false,value = "店铺id")
    private Long shopId;


}
