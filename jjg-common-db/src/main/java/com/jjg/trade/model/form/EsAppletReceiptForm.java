package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 小程序-发票
 */
@Data
@ApiModel
public class EsAppletReceiptForm implements Serializable {

    private static final long serialVersionUID = -6389742728556211209L;

    /**
     * 普票类型
     */
    @ApiModelProperty(value = "普票类型，0为个人，其他为公司", required = false)
    private Integer type;

    /**
     * 发票类型
     */
/*    @ApiModelProperty(value = "枚举，ELECTRO:电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票", required = false, hidden = true)
    private String receiptType;*/

    /**
     * 发票抬头
     */
    @ApiModelProperty(value = "发票抬头", required = true)
    @NotBlank(message = "发票抬头不能为空")
    private String receiptTitle;
    /**
     * 发票内容
     */
    @ApiModelProperty(value = "发票内容", required = true)
    @NotBlank(message = "发票内容不能为空")
    private String receiptContent;
    /**
     * 发票税号
     */
    @ApiModelProperty(value = "发票税号", required = false)
    private String taxNo;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

}
