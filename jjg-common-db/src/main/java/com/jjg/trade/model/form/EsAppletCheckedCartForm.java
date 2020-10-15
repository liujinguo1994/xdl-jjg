package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 小程序-全选或全不选
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-30
 */
@Data
@ApiModel
public class EsAppletCheckedCartForm implements Serializable {
    private static final long serialVersionUID = 3863114069985817704L;

    /**
     * TAB页显示
     */
    @ApiModelProperty(required = false,value = "TAB页显示",example = "priceDown,stockShortage")
    private String tab;

    /**
     * 是否选中
     */
    @ApiModelProperty(required = false,value = "是否选中",example = "0,1")
    @Min(message = "必须为数字且,1为开启,0为关闭", value = 0)
    @Max(message = "必须为数字且,1为开启,0为关闭", value = 1)
    private Integer checked;


    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;
}
