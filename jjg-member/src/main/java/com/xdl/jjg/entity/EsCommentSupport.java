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
 * 评论点赞表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-07 16:55:59
 */
@Data
@Accessors(chain = true)
@TableName("es_comment_support")
public class EsCommentSupport extends Model<EsCommentSupport> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 评论主键id
	 */
	@TableField("comment_id")
	private Long commentId;

	/**
	 * 会员id
	 */
	@TableField("member_id")
	private Long memberId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
