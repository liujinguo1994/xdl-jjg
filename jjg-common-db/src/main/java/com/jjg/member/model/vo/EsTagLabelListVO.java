package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel
public class EsTagLabelListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品标签
     */
    @ApiModelProperty(required = false, value = "商品标签")
    private List<EsCommentLabelVO> GoodsTag;
    /**
     * 物流标签
     */
    @ApiModelProperty(required = false, value = "物流标签")
    private List<EsCommentLabelVO> deliveryTag;
    /**
     * 服务标签
     */
    @ApiModelProperty(required = false, value = "服务标签")
    private List<EsCommentLabelVO> serviceTag;

}
