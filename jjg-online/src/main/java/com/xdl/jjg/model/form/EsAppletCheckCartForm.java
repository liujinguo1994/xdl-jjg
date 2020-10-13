package com.xdl.jjg.model.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-选中购物车
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-30
 */
@Data
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EsAppletCheckCartForm implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;

    @ApiModelProperty(required = true,value = "skuid",example = "1")
    @NotNull(message = "skuId不能为空")
    private Long skuId;

    @ApiModelProperty(required = false,value = "数量")
    private Integer num;

    @ApiModelProperty(required = false,value = "是否选中",example = "0,1")
    @Min(message = "必须为数字且,1为开启,0为关闭", value = 0)
    @Max(message = "必须为数字且,1为开启,0为关闭", value = 1)
    private Integer checked;


    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

}
