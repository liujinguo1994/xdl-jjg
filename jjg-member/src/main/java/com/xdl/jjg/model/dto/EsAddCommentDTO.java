package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 追加评论
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-10-08 15:19:24
 */
@Data
@ToString
public class EsAddCommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 评论id
     */
	private Long commentId;

    /**
     * 追加内容
     */
	private String content;

    /**
     * 图片地址
     */
    private String original;
}
