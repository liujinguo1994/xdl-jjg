package com.jjg.trade.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
     * 追加评论ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long addCommentId;

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
