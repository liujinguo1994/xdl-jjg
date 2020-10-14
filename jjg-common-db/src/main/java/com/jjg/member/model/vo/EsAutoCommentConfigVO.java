package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统自动评论VO
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-16 14:19:41
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsAutoCommentConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@ApiModelProperty(value = "主键")
	private Long id;

    /**
     * 自动评论内容
     */
	@ApiModelProperty(value = "自动评论内容")
	private String autoComment;

    /**
     * 自动评论天数
     */
	@ApiModelProperty(value = "自动评论天数")
	private Integer autoDays;

	protected Serializable pkVal() {
		return this.id;
	}

}
