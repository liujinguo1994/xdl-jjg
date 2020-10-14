package com.jjg.member.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 自动评价配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-16 14:19:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsAutoCommentConfigDO extends Model<EsAutoCommentConfigDO> {

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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
