package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 编辑店铺信息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-26 10:18:18
 */
@Data
@ApiModel(description = "编辑店铺信息")
public class EsUpdateShopInfoForm implements Serializable {

    /**
     * 是否审核通过(0通过并保存，1拒绝通过),只有审核时传此字段
     */
    @ApiModelProperty(value = "是否审核通过(0通过并保存，1拒绝通过),只有审核时传此字段", example = "1")
    private Integer pass;

//------------------------------------店铺信息--------------------------------------------
    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID", example = "1")
    private Long id;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
     * 店铺所在省id
     */
    @ApiModelProperty(value = "店铺所在省id", example = "1")
    private Long shopProvinceId;
    /**
     * 店铺所在市id
     */
    @ApiModelProperty(value = "店铺所在市id", example = "1")
    private Long shopCityId;
    /**
     * 店铺所在县id
     */
    @ApiModelProperty(value = "店铺所在县id", example = "1")
    private Long shopCountyId;
    /**
     * 店铺所在镇id
     */
    @ApiModelProperty(value = "店铺所在镇id", example = "1")
    private Long shopTownId;
    /**
     * 店铺所在省名称
     */
    @ApiModelProperty(value = "店铺所在省名称")
    private String shopProvince;
    /**
     * 店铺所在市名称
     */
    @ApiModelProperty(value = "店铺所在市名称")
    private String shopCity;
    /**
     * 店铺所在县名称
     */
    @ApiModelProperty(value = "店铺所在县名称")
    private String shopCounty;
    /**
     * 店铺所在镇名称
     */
    @ApiModelProperty(value = "店铺所在镇名称")
    private String shopTown;
    /**
     * 店铺详细地址
     */
    @ApiModelProperty(value = "店铺详细地址")
    private String shopAdd;
    /**
     * 佣金比例
     */
    @ApiModelProperty(value = "佣金比例", example = "1")
    private Double commission;
//----------------------------------------基本信息------------------------------------
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    /**
     * 公司地址
     */
    @ApiModelProperty(value = "公司地址")
    private String companyAddress;
    /**
     * 公司电话
     */
    @ApiModelProperty(value = "公司电话")
    private String companyPhone;
    /**
     * 电子邮箱
     */
    @ApiModelProperty(value = "电子邮箱")
    private String companyEmail;
    /**
     * 员工总数
     */
    @ApiModelProperty(value = "员工总数", example = "1")
    private Integer employeeNum;
    /**
     * 注册资金
     */
    @ApiModelProperty(value = "注册资金", example = "1")
    private Double regMoney;
    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名")
    private String linkName;
    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String linkPhone;
    /**
     * 是否自营
     */
    @ApiModelProperty(value = "是否自营", example = "1")
    private Integer selfOperated;
//-----------------------------------营业执照信息-----------------------------------------
    /**
     * 法人姓名
     */
    @ApiModelProperty(value = "法人姓名")
    private String legalName;
    /**
     * 法人身份证
     */
    @ApiModelProperty(value = "法人身份证")
    private String legalId;
    /**
     * 营业执照号
     */
    @ApiModelProperty(value = "营业执照号")
    private String licenseNum;
    /**
     * 法定经营范围
     */
    @ApiModelProperty(value = "法定经营范围")
    private String scope;
    /**
     * 营业执照所在省id
     */
    @ApiModelProperty(value = "营业执照所在省id", example = "1")
    private Long licenseProvinceId;
    /**
     * 营业执照所在市id
     */
    @ApiModelProperty(value = "营业执照所在市id", example = "1")
    private Long licenseCityId;
    /**
     * 营业执照所在县id
     */
    @ApiModelProperty(value = "营业执照所在县id", example = "1")
    private Long licenseCountyId;
    /**
     * 营业执照所在镇id
     */
    @ApiModelProperty(value = "营业执照所在镇id", example = "1")
    private Long licenseTownId;
    /**
     * 营业执照所在省名称
     */
    @ApiModelProperty(value = "营业执照所在省名称")
    private String licenseProvince;
    /**
     * 营业执照所在市名称
     */
    @ApiModelProperty(value = "营业执照所在市名称")
    private String licenseCity;
    /**
     * 营业执照所在县
     */
    @ApiModelProperty(value = "营业执照所在县")
    private String licenseCounty;
    /**
     * 营业执照所在镇
     */
    @ApiModelProperty(value = "营业执照所在镇")
    private String licenseTown;
    /**
     * 营业执照详细地址
     */
    @ApiModelProperty(value = "营业执照详细地址")
    private String licenseAdd;
    /**
     * 成立日期
     */
    @ApiModelProperty(value = "成立日期", example = "1")
    private Long establishDate;
    /**
     * 营业执照有效期开始
     */
    @ApiModelProperty(value = "营业执照有效期开始", example = "1")
    private Long licenceStart;
    /**
     * 营业执照有效期结束
     */
    @ApiModelProperty(value = "营业执照有效期结束", example = "1")
    private Long licenceEnd;
    /**
     * 法人身份证照片
     */
    @ApiModelProperty(value = "法人身份证照片")
    private String legalImg;
    /**
     * 营业执照电子版
     */
    @ApiModelProperty(value = "营业执照电子版")
    private String licenceImg;
//-----------------------------------------组织机构信息-----------------------------------
    /**
     * 组织机构代码
     */
    @ApiModelProperty(value = "组织机构代码")
    private String organizationCode;
    /**
     * 组织机构电子版
     */
    @ApiModelProperty(value = "组织机构电子版")
    private String codeImg;
//---------------------------------------开户银行许可证------------------------------------
    /**
     * 银行开户名
     */
    @ApiModelProperty(value = "银行开户名")
    private String bankAccountName;
    /**
     * 银行开户账号
     */
    @ApiModelProperty(value = "银行开户账号")
    private String bankNumber;
    /**
     * 开户银行支行名称
     */
    @ApiModelProperty(value = "开户银行支行名称")
    private String bankName;
    /**
     * 开户银行所在省id
     */
    @ApiModelProperty(value = "开户银行所在省id", example = "1")
    private Long bankProvinceId;
    /**
     * 开户银行所在市id
     */
    @ApiModelProperty(value = "开户银行所在市id", example = "1")
    private Long bankCityId;
    /**
     * 开户银行所在县id
     */
    @ApiModelProperty(value = "开户银行所在县id", example = "1")
    private Long bankCountyId;
    /**
     * 开户银行所在镇id
     */
    @ApiModelProperty(value = "开户银行所在镇id", example = "1")
    private Long bankTownId;
    /**
     * 开户银行所在省名称
     */
    @ApiModelProperty(value = "开户银行所在省名称")
    private String bankProvince;
    /**
     * 开户银行所在市名称
     */
    @ApiModelProperty(value = "开户银行所在市名称")
    private String bankCity;
    /**
     * 开户银行所在县名称
     */
    @ApiModelProperty(value = "开户银行所在县名称")
    private String bankCounty;
    /**
     * 开户银行所在镇民名称
     */
    @ApiModelProperty(value = "开户银行所在镇民名称")
    private String bankTown;
    /**
     * 开户银行许可证电子版
     */
    @ApiModelProperty(value = "开户银行许可证电子版")
    private String bankImg;
//----------------------------------------税务登记---------------------------------------------
    /**
     * 税务登记证号
     */
    @ApiModelProperty(value = "税务登记证号")
    private String taxesCertificateNum;
    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String taxesDistinguishNum;
    /**
     * 一般纳税人证明电子版
     */
    @ApiModelProperty(value = "一般纳税人证明电子版")
    private String taxesImg;
    /**
     * 税务登记证书
     */
    @ApiModelProperty(value = "税务登记证书")
    private String taxesCertificateImg;
//-----------------------------------------经营类目-----------------------------------
    /**
     * 店铺经营类目
     */
    @ApiModelProperty(value = "店铺经营类目")
    private String goodsManagementCategory;
}
