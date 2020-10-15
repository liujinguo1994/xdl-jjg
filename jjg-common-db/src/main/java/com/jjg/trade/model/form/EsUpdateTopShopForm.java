package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺置顶操作
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
public class EsUpdateTopShopForm implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 操作类型 1取消置顶，2置顶
     */
    @ApiModelProperty(required = true, value = "操作类型 1取消置顶，2置顶",example = "1")
    private Integer sort;
    /**
     * 店铺id
     */
    @ApiModelProperty(required = true, value = "店铺id",example = "1")
    private Long id;


}
