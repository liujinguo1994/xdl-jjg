package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 追加评论
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-10-08 15:19:25
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsAddCommentForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
	@ApiModelProperty(required = true,value = "评论id",example = "12")
	private Long commentId;

    /**
     * 追加内容
     */
	@ApiModelProperty(value = "追加内容")
	private String content;

	/**
	 * 追加图片
	 */
	@ApiModelProperty(value = "追加图片,以逗号分隔开")
	private String original;

}
