package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *  追加评论图片VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-12 14:44:46
 */
@Data
@Api
@Accessors(chain = true)
public class EsAddCommentPictureVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@ApiModelProperty(value = "主键")
	private Long id;

    /**
     * 评论ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "评论ID")
	private Long commentId;

    /**
     * 图片路径
     */
	@ApiModelProperty(value = "图片路径")
	private String original;

    /**
     * 排序
     */
	@ApiModelProperty(value = "排序")
	private Integer sort;

	protected Serializable pkVal() {
		return this.id;
	}

}
