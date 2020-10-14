package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 评论分类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-29 14:18:13
 */
@Data
@ToString
public class EsCommentSortConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 评分最小值
     */
	private Double minScore;

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
