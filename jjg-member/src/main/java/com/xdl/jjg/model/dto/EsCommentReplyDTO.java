package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 评论回复(店家回复)
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsCommentReplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;

    /**
     * 评论id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long commentId;

    /**
     * 回复父id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

    /**
     * 回复内容
     */
	private String content;

    /**
     * 创建时间
     */
	private Long createTime;

	/**
	 * 店铺id
	 */
	private Long shopId;

    /**
     * 商家或者买家
     */
	private String role;

    /**
     * 父子路径0|10|
     */
	private String path;

	protected Serializable pkVal() {
		return this.id;
	}

}
