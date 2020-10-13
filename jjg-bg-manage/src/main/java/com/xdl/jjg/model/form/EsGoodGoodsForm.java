package com.xdl.jjg.model.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;


/**
 * <p>
 * 品质好货
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-08-06
 */
@Data
@ApiModel
public class EsGoodGoodsForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", example = "1")
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty(required = true, value = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    /**
     * 商品链接
     */
    @ApiModelProperty(required = true, value = "商品链接")
    @NotBlank(message = "商品链接不能为空")
    private String goodsUrl;

    /**
     * 图片地址
     */
    @ApiModelProperty(required = true, value = "图片地址")
    @NotBlank(message = "图片地址不能为空")
    private String picUrl;

}
