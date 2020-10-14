package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 *  前端控制器-小程序-查看售后明细
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-17
 */
@ApiModel
@Data
public class AppletAfterSaleDetailForm implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;

    @ApiModelProperty(name = "sn", value = "售后单编号",required = true)
    @NotBlank(message = "售后单编号不能为空")
    private String sn;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

}
