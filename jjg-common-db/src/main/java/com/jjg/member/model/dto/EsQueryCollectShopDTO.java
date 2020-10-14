package com.jjg.member.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 查询收藏店铺列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsQueryCollectShopDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 店铺名称
     */
    private String keyword;
    /**
     * 标签tagId
     */
    private Long tagId;

    /**
     * 会员ID
     */
    private Long memberId;

}
