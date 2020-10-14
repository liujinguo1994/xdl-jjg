package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 发票
 *
 * @author libw 981087977@qq.com
 * @since 2019-07-01
 */
@Data
@ApiModel(description = "发票")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptVO implements Serializable {

    private static final long serialVersionUID = -6389742728556211209L;

    /**
     * 普票类型
     */
    @ApiModelProperty(name = "type", value = "普票类型，0为个人，其他为公司", required = false)
    private Integer type;

    /**
     * 发票类型
     */
    @ApiModelProperty(name = "receipt_type", value = "枚举，ELECTRO:电子普通发票，VATORDINARY：增值税普通发票，VATOSPECIAL：增值税专用发票", required = false, hidden = true)
    private String receiptType;
    /**
     * 发票抬头
     */
    @ApiModelProperty(name = "receipt_title", value = "发票抬头", required = false)
    private String receiptTitle;
    /**
     * 发票内容
     */
    @ApiModelProperty(name = "receipt_content", value = "发票内容", required = false)
    private String receiptContent;
    /**
     * 发票税号
     */
    @ApiModelProperty(name = "tax_no", value = "发票税号", required = false)
    private String taxNo;

}
