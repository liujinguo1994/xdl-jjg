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
 * 追加评论
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-10-08 15:19:22
 */
@Data
@Accessors(chain = true)
@TableName("es_add_comment")
public class EsAddComment extends Model<EsAddComment> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 评论id
	 */
	@TableField("comment_id")
	private Long commentId;

	/**
	 * 追加内容
	 */
	@TableField("content")
	private String content;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
