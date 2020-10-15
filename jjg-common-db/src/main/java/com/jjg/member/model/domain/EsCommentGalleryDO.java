package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  评论图片
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsCommentGalleryDO implements Serializable {


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


}
