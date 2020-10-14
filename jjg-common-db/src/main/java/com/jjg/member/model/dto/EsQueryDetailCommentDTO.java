package com.jjg.member.model.dto;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 商品详情页面评价查询
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-02 19:50:03
 */
@Data
@ToString
public class EsQueryDetailCommentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 查询类型3带图评论，0好评，1中评，2差评,3最近（规则是三个月以内评价）4追评
     */
    private Integer grade;
    /**
     * 1 带图片评论
     */
    private Integer imageType;
    /**
     * 商品id
     */
    private Long goodsId;


}
