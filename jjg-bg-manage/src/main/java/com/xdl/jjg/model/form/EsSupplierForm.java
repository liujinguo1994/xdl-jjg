package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 供应商
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-26 10:18:18
 */
@Data
@ApiModel
public class EsSupplierForm implements Serializable {


    private static final long serialVersionUID = 7054068478466538248L;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 联系人名称
     */
    @ApiModelProperty(value = "联系人名称")
    private String contactName;

    /**
     * 联系人手机号码
     */
    @ApiModelProperty(value = "联系人手机号码")
    private String mobile;

    /**
     * 固定电话
     */
    @ApiModelProperty(value = "固定电话")
    private String telephone;

    /**
     * 公司详细地址
     */
    @ApiModelProperty(value = "公司详细地址")
    private String companyAddr;

    /**
     * 企业法人
     */
    @ApiModelProperty(value = "企业法人")
    private String enterprisePerson;

    /**
     * 公司地址所在省ID
     */
    @ApiModelProperty(value = "公司地址所在省ID", example = "1")
    private Long provinceId;

    /**
     * 公司地址所在市ID
     */
    @ApiModelProperty(value = "公司地址所在市ID", example = "1")
    private Long cityId;

    /**
     * 公司地址所在区ID
     */
    @ApiModelProperty(value = "公司地址所在区ID", example = "1")
    private Long countyId;

    /**
     * 公司地址所在省名称
     */
   /* @ApiModelProperty(value = "公司地址所在省名称")
    private String province;*/

    /**
     * 公司地址所在市名称
     */
   /* @ApiModelProperty(value = "公司地址所在市名称")
    private String city;*/

    /**
     * 公司地址所在区名称
     */
    /*@ApiModelProperty(value = "公司地址所在区名称")
    private String county;*/

    /**
     * 开票信息开户银行
     */
    @ApiModelProperty(value = "开票信息开户银行")
    private String openBank;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String identification;

    /**
     * 地址电话
     */
    @ApiModelProperty(value = "地址电话")
    private String openMobile;

    /**
     * 转账开户银行
     */
    @ApiModelProperty(value = "转账开户银行")
    private String transferBank;

    /**
     * 转账银行账号
     */
    @ApiModelProperty(value = "转账银行账号")
    private String transferAccount;

    /**
     * 合作开始时间
     */
    @ApiModelProperty(value = "合作开始时间", example = "1")
    private Long cooperationStartTime;

    /**
     * 合作结束时间
     */
    @ApiModelProperty(value = "合作结束时间", example = "1")
    private Long cooperationEndTime;

    /**
     * 合作年限
     */
    @ApiModelProperty(value = "合作年限", example = "1")
    private Double years;

    /**
     * 营业执照
     */
    @ApiModelProperty(value = "营业执照")
    private String license;

    /**
     * 经销合同
     */
    @ApiModelProperty(value = "经销合同")
    private String contract;

    /**
     * 授权书1
     */
    @ApiModelProperty(value = "授权书1")
    private String empower1;

    /**
     * 授权书2
     */
    @ApiModelProperty(value = "授权书2")
    private String empower2;

}
