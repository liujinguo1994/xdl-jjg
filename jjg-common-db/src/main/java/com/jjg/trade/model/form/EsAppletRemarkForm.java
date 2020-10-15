package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-订单备注
 * </p>
 */
@Data
@ApiModel
public class EsAppletRemarkForm implements Serializable {
    private static final long serialVersionUID = 3863114069985817704L;
    /**
     * 店铺id
     */
    @ApiModelProperty(required = true,value = "店铺id")
    @NotNull(message = "店铺id不能为空")
    private Long shopId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

}
