package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 降价和失效商品数量
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsCutAndEffectVO implements Serializable {

    /**
     * 降价商品数量
     */
    @ApiModelProperty(required = false, value = "降价商品数量", example = "0")
    private Integer cutPricNum;
    /**
     * 失效商品数量
     */
    @ApiModelProperty(required = false, value = "失效商品数量", example = "0")
    private Integer effectNum;


}
