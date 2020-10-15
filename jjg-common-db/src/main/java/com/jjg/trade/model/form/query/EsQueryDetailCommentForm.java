package com.jjg.trade.model.form.query;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel

public class EsQueryDetailCommentForm implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 查询类型0:好评，1中评，2差评,
     */
    @ApiModelProperty(required = false, value = "查询类型0:好评，1中评，2差评，3最近（规则是三个月以内评价）4追评")
    private Integer grade;
    /**
     * 是否带图片：1 有, 2 无
     */
    @ApiModelProperty(required = false, value = "是否带图片：1 有, 2 无")
    private Integer imageType;
    /**
     * 商品id
     */
    @ApiModelProperty(required = true, value = "商品id")
    private Long goodsId;


}
