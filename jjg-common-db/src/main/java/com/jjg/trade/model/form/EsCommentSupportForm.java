package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 点赞评论Form
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-07 16:56:01
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsCommentSupportForm implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;
	/**
	 * 评论主键id
     */
	@ApiModelProperty(required = true,value = "评论主键id")
	private Long commentId;

    /**
     * 会员id
     */
	@ApiModelProperty(value = "会员id")
	private Long memberId;

}
