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
 * 系统自动评论设置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-16 14:19:40
 */
@Data
@Accessors(chain = true)
@TableName("es_auto_comment_config")
public class EsAutoCommentConfig extends Model<EsAutoCommentConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 自动评论内容
	 */
	@TableField("auto_comment")
	private String autoComment;

	/**
	 * 自动评论天数
	 */
	@TableField("auto_days")
	private Integer autoDays;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
