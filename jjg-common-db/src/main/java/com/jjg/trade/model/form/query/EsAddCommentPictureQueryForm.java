package com.jjg.trade.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *  追加评论图片QueryForm
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-12 14:44:48
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsAddCommentPictureQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 追加评论ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "追加评论ID")
	private Long addCommentId;

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

}
