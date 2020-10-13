package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *  追加评论图片
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-12 14:44:43
 */
@Data
@Accessors(chain = true)
@TableName("es_add_comment_picture")
public class EsAddCommentPicture extends Model<EsAddCommentPicture> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 追加评论ID
	 */
	@TableField("add_comment_id")
	private Long addCommentId;

	/**
	 * 图片路径
	 */
	@TableField("original")
	private String original;

	/**
	 * 排序
	 */
	@TableField("sort")
	private Integer sort;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
