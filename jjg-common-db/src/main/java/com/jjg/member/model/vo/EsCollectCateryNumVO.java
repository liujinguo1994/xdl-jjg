package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品分类名称和数量
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsCollectCateryNumVO implements Serializable {

    /**
     * 二级分类id
     */
    @ApiModelProperty(required = false, value = "二级分类id", example = "1")
    private Long categoryId;
    /**
     * 分类名称
     */
    @ApiModelProperty(required = false,value = "分类名称")
    private String categoryName;
    /**
     * 分类数量
     */
    @ApiModelProperty(required = false, value = "分类数量", example = "1")
    Integer categoryNum;


}
