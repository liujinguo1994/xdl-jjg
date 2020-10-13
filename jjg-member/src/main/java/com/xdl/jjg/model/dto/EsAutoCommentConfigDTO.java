package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统自动评论配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-16 14:19:41
 */
@Data
@Accessors(chain = true)
public class EsAutoCommentConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;

    /**
     * 自动评论内容
     */
	private String autoComment;

    /**
     * 自动评论天数
     */
	private Integer autoDays;

	protected Serializable pkVal() {
		return this.id;
	}

}
