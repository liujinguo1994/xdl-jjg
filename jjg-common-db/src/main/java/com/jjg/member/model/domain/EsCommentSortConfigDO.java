package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 评分分类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-29 14:18:12
 */
@Data
public class EsCommentSortConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 评分最小值
     */
	private Double minScore;

	/**
	 * 评分最大值
	 */
	private Double maxScore;

    /**
     * 评论分类名称
     */
	private String commentSort;

    /**
     * 评论分类类型 0:好评,1:中评.2:差评
     */
	private Integer commentType;

}
