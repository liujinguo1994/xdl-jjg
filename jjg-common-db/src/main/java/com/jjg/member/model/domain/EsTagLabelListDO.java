package com.jjg.member.model.domain;

import com.jjg.member.model.domain.EsCommentLabelDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品评论标签内容
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsTagLabelListDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品标签
     */
    private List<EsCommentLabelDO> GoodsTag;
    /**
     * 物流标签
     */
    private List<EsCommentLabelDO> deliveryTag;
    /**
     * 服务标签
     */
    private List<EsCommentLabelDO> serviceTag;

}
