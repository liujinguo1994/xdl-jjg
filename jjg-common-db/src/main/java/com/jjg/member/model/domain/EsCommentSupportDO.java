package com.jjg.member.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 评论点赞
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-09-07 16:56:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsCommentSupportDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;

    /**
     * 评论主键id
     */
	private Long commentId;

    /**
     * 会员id
     */
	private Long memberId;

}
