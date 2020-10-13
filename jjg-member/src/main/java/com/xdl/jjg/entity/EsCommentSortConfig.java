package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-29 14:18:11
 */
@Data
@TableName("es_comment_sort_config")
public class EsCommentSortConfig extends Model<EsCommentSortConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id")
	private Long id;

	/**
	 * 评分最小值
	 */
	@TableField("min_score")
	private Double minScore;

	@TableField("max_score")
	private Double maxScore;

	/**
	 * 评论分类名称
	 */
	@TableField("comment_sort")
	private String commentSort;

	/**
	 * 评论分类类型 0:好评,1:中评.2:差评
	 */
	@TableField("comment_type")
	private Integer commentType;

}
