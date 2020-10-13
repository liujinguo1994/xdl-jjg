package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  追加评论图片
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-12 14:44:45
 */
@Data
public class EsAddCommentPictureDO extends Model<EsAddCommentPictureDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;

    /**
     * 评论ID
     */
	private Long commentId;

    /**
     * 图片路径
     */
	private String original;

    /**
     * 排序
     */
	private Integer sort;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
