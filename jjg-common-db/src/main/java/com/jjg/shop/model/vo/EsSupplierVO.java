package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSupplierVO implements Serializable {



	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 供应商编号
	 */
	private String supplierCode;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 创建时间
	 */

	private Long createTime;

	/**
	 * 更新时间
	 */

	private Long updateTime;

	/**
	 * 是否有效 0 有效 1 无效
	 */
	private Integer state;

	/**
	 * 联系人名称
	 */
	private String contactName;

	/**
	 * 联系人手机号码
	 */
	private String mobile;

	/**
	 * 固定电话
	 */
	private String telephone;

	/**
	 * 公司详细地址
	 */
	private String companyAddr;

	/**
	 * 企业法人
	 */
	private String enterprisePerson;

	/**
	 * 公司地址所在省ID
	 */
	private Long provinceId;

	/**
	 * 公司地址所在市ID
	 */
	private Long cityId;

	/**
	 * 公司地址所在区ID
	 */
	private Long countyId;

	/**
	 * 公司地址所在省名称
	 */
	private String province;

	/**
	 * 公司地址所在市名称
	 */
	private String city;

	/**
	 * 公司地址所在区名称
	 */
	private String county;

	/**
	 * 开票信息开户银行
	 */
	private String openBank;

	/**
	 * 纳税人识别号
	 */
	private String identification;

	/**
	 * 地址电话
	 */
	private String openMobile;

	/**
	 * 转账开户银行
	 */
	private String transferBank;

	/**
	 * 转账银行账号
	 */
	private String transferAccount;

	/**
	 * 合作开始时间
	 */
	private Long cooperationStartTime;

	/**
	 * 合作结束时间
	 */
	private Long cooperationEndTime;

	/**
	 * 合作年限
	 */
	private Double years;

	/**
	 * 营业执照
	 */
	private String license;

	/**
	 * 经销合同
	 */
	private String contract;

	/**
	 * 授权书1
	 */
	private String empower1;

	/**
	 * 授权书2
	 */
	private String empower2;

}
