package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺详细VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:27
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsShopDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺详细id
     */
	@ApiModelProperty(value = "店铺详细id")
	private Long id;

    /**
     * 店铺id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺id")
	private Long shopId;

    /**
     * 店铺所在省id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺所在省id")
	private Long shopProvinceId;

    /**
     * 店铺所在市id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺所在市id")
	private Long shopCityId;

    /**
     * 店铺所在县id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺所在县id")
	private Long shopCountyId;

    /**
     * 店铺所在镇id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺所在镇id")
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
	@ApiModelProperty(value = "员工总数")
	private Integer employeeNum;

    /**
     * 注册资金
     */
	@ApiModelProperty(value = "注册资金")
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
     * 法人身份证照片
     */
	@ApiModelProperty(value = "法人身份证照片")
	private String legalImg;

    /**
     * 营业执照号
     */
	@ApiModelProperty(value = "营业执照号")
	private String licenseNum;

    /**
     * 营业执照所在省id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "营业执照所在省id")
	private Long licenseProvinceId;

    /**
     * 营业执照所在市id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "营业执照所在市id")
	private Long licenseCityId;

    /**
     * 营业执照所在县id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "营业执照所在县id")
	private Long licenseCountyId;

    /**
     * 营业执照所在镇id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "营业执照所在镇id")
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
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "成立日期")
	private Long establishDate;

    /**
     * 营业执照有效期开始
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "营业执照有效期开始")
	private Long licenceStart;

    /**
     * 营业执照有效期结束
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "营业执照有效期结束")
	private Long licenceEnd;

    /**
     * 法定经营范围
     */
	@ApiModelProperty(value = "法定经营范围")
	private String scope;

    /**
     * 营业执照电子版
     */
	@ApiModelProperty(value = "营业执照电子版")
	private String licenceImg;

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

    /**
     * 一般纳税人证明电子版
     */
	@ApiModelProperty(value = "一般纳税人证明电子版")
	private String taxesImg;

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
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "开户银行所在省id")
	private Long bankProvinceId;

    /**
     * 开户银行所在市id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "开户银行所在市id")
	private Long bankCityId;

    /**
     * 开户银行所在县id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "开户银行所在县id")
	private Long bankCountyId;

    /**
     * 开户银行所在镇id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "开户银行所在镇id")
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
     * 税务登记证书
     */
	@ApiModelProperty(value = "税务登记证书")
	private String taxesCertificateImg;

    /**
     * 店铺经营类目
     */
	@ApiModelProperty(value = "店铺经营类目")
	private String goodsManagementCategory;

    /**
     * 店铺等级
     */
	@ApiModelProperty(value = "店铺等级")
	private Integer shopLevel;

    /**
     * 店铺等级申请
     */
	@ApiModelProperty(value = "店铺等级申请")
	private Integer shopLevelApply;

    /**
     * 店铺相册已用存储量
     */
	@ApiModelProperty(value = "店铺相册已用存储量")
	private Double storeSpaceCapacity;

    /**
     * 店铺logo
     */
	@ApiModelProperty(value = "店铺logo")
	private String shopLogo;

    /**
     * 店铺横幅
     */
	@ApiModelProperty(value = "店铺横幅")
	private String shopBanner;

    /**
     * 店铺简介
     */
	@ApiModelProperty(value = "店铺简介")
	private String shopDesc;

    /**
     * 是否推荐
     */
	@ApiModelProperty(value = "是否推荐")
	private Integer shopRecommend;

    /**
     * 店铺主题id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺主题id")
	private Long shopThemeid;

    /**
     * 店铺主题模版路径
     */
	@ApiModelProperty(value = "店铺主题模版路径")
	private String shopThemePath;

    /**
     * 店铺主题id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "店铺主题id")
	private Long wapThemeid;

    /**
     * wap店铺主题
     */
	@ApiModelProperty(value = "wap店铺主题")
	private String wapThemePath;

    /**
     * 店铺信用
     */
	@ApiModelProperty(value = "店铺信用")
	private Integer shopCredit;

    /**
     * 店铺好评率
     */
	@ApiModelProperty(value = "店铺好评率")
	private Double shopPraiseRate;

    /**
     * 店铺描述相符度
     */
	@ApiModelProperty(value = "店铺描述相符度")
	private Double shopDescriptionCredit;

    /**
     * 服务态度分数
     */
	@ApiModelProperty(value = "服务态度分数")
	private Double shopServiceCredit;

    /**
     * 发货速度分数
     */
	@ApiModelProperty(value = "发货速度分数")
	private Double shopDeliveryCredit;

    /**
     * 店铺收藏数
     */
	@ApiModelProperty(value = "店铺收藏数")
	private Integer shopCollect;

    /**
     * 店铺商品数
     */
	@ApiModelProperty(value = "店铺商品数")
	private Integer goodsNum;

    /**
     * 店铺客服qq
     */
	@ApiModelProperty(value = "店铺客服qq")
	private String shopQq;

    /**
     * 是否自营
     */
	@ApiModelProperty(value = "是否自营")
	private Integer selfOperated;

    /**
     * 申请开店进度
     */
	@ApiModelProperty(value = "申请开店进度")
	private Integer step;
	/**
	 * 供应商ID
	 */
	@ApiModelProperty(value = "供应商ID")
	private Long supplierId;
	/**
	 * 供应商名称
	 */
	@ApiModelProperty(value = "供应商名称")
	private String supplierName;

	/**
	 * 手机端背景图片
	 */
	@ApiModelProperty(value = "手机端背景图片")
	private String backImage;
	private String bannerUrl;
	protected Serializable pkVal() {
		return this.id;
	}

}
