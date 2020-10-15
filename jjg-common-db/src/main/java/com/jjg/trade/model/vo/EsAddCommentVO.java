package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 追加评论VO
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-10-08 15:19:24
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsAddCommentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@ApiModelProperty(value = "主键id")
	private Long id;

    /**
     * 评论id
     */
	@ApiModelProperty(value = "评论id")
	private Long commentId;

    /**
     * 追加内容
     */
	@ApiModelProperty(value = "追加内容")
	private String content;

	@ApiModelProperty(value = "追加图片")
	private CommentImageVO original;

}
