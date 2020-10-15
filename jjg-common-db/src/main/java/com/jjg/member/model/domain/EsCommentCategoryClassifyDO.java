package com.jjg.member.model.domain;

import com.jjg.member.model.domain.EsCommentCategoryDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品评论标签关联分类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
public class EsCommentCategoryClassifyDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 0商品
     */
    private List<EsCommentCategoryDO> goodLabels;
    /**
     * 1物流
     */
    private List<EsCommentCategoryDO> expressLabels;
    /**
     * 2服务
     */
    private List<EsCommentCategoryDO> serviceLabels;
}
