package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *  追加评论图片
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-12 14:44:46
 */
@Data
@Accessors(chain = true)
public class EsAddCommentPictureDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;

    /**
     * 评论ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long commentId;

    /**
     * 图片路径
     */
	private String original;

    /**
     * 排序
     */
	private Integer sort;

	protected Serializable pkVal() {
		return this.id;
	}

}
