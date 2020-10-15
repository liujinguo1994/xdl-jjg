package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-添加购物车
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-29
 */
@Data
@ApiModel
public class EsAppletAddCartForm implements Serializable {

    private static final long serialVersionUID = 3863114069985817704L;

    @ApiModelProperty(required = true,value = "skuid",example = "1")
    @NotNull(message = "skuId不能为空")
    private Long skuId;

    @ApiModelProperty(required = true,value = "数量")
    @NotNull(message = "购买数量必填")
    private Integer num;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

}
