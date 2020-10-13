package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 小程序-商品收藏
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-14
 */
@Data
@ApiModel
public class EsAppletGoodsCollectionForm implements Serializable {

    private static final long serialVersionUID = -785021414818941920L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true,value = "商品ID")
    @NotBlank(message = "商品ID不能为空")
    private Long goodsId;
}
