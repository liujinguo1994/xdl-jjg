package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 我的卓付订单查询
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsQueryZfOrdesForm implements Serializable {

    /**
     * 查询类型1待付款，2待发货，3待收货
     */
    @ApiModelProperty(required = false, value = "查询类型1待付款，2待发货，3待收货")
    private Integer type;

}
