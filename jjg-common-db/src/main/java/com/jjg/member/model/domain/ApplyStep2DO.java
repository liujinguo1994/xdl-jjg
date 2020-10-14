package com.xdl.jjg.model.domain;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 申请开店第二步
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-08
 */
@Data
@ToString
public class ApplyStep2DO {
	/**
	 * 法人姓名
	 */
	private String legalName;
	/**
	 * 法人身份证
	 */
	private String legalId;
	/**
	 * 法人身份证照片
	 */
	private String legalImg;
	/**
	 * 营业执照号
	 */
	private String licenseNum;
	/**
	 * 营业执照所在省id
	 */
	private Long licenseProvinceId;
	/**
	 * 营业执照所在市id
	 */
	private Long licenseCityId;
	/**
	 * 营业执照所在县id
	 */
	private Long licenseCountyId;
	/**
	 * 营业执照所在镇id
	 */
	private Long licenseTownId;
	/**
	 * 营业执照所在省
	 */
	private String licenseProvince;
	/**
	 * 营业执照所在市
	 */
	private String licenseCity;
	/**
	 * 营业执照所在县
	 */
	private String licenseCounty;
	/**
	 * 营业执照所在镇
	 */
	private String licenseTown;
	/**
	 * 营业执照详细地址
	 */
	private String licenseAdd;
	/**
	 * 成立日期
	 */
	private Long establishDate;
	/**
	 * 营业执照有效期开始
	 */
	private Long licenceStart;
	/**
	 * 营业执照有效期结束
	 */
	private Long licenceEnd;
	/**
	 * 法定经营范围
	 */
	private String scope;
	/**
	 * 营业执照电子版
	 */
	private String licenceImg;
	/**
	 * 组织机构代码
	 */
	private String organizationCode;
	/**
	 * 组织机构电子版
	 */
	private String codeImg;
	/**
	 * 一般纳税人证明电子版
	 */
	private String taxesImg;
	/**
	 * 申请开店进度
	 */
	private Integer step;

}