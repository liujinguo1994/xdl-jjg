package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品评论标签关联分类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
public class EsCommentCategoryDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;
    /**
     * 评论标签id
     */
    private Long labelId;
    /**
     * 商品分类id
     */
    private Long categoryId;
    /**
     * 标记是否被绑定 true绑定，false未绑定
     */
    private Boolean selected;
    /**
     * 评论标签内容
     */
    private String commentLabel;
    /**
     * 标签类型 0：商品，1物流，2服务
     */
    private Integer type;

}
