package com.jjg.member.model.dto;

import com.shopx.common.model.result.QueryPageForm;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 查询会员商品收藏
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsQueryMemberCollectionGoodsDTO extends QueryPageForm {

    private static final long serialVersionUID = 1L;

    /**
     * 查询类型 0全部，1降价，2失效
     */
    private Integer type;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 会员id
     */
    private Long userId;

}
