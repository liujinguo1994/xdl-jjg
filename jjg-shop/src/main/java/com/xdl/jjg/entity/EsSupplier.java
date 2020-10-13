package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-06-26 10:18:17
 */
@Data
@Accessors(chain = true)
@TableName("es_supplier")
public class EsSupplier extends Model<EsSupplier> {

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 供应商编号
	 */
	@TableField("supplier_code")
	private String supplierCode;

	/**
	 * 供应商名称
	 */
	@TableField("supplier_name")
	private String supplierName;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;

	/**
	 * 是否有效 0 有效 1 无效
	 */
	@TableField("state")
	private Integer state;

	/**
	 * 联系人名称
	 */
	@TableField("contact_name")
	private String contactName;

	/**
	 * 联系人手机号码
	 */
	@TableField("mobile")
	private String mobile;

	/**
	 * 固定电话
	 */
	@TableField("telephone")
	private String telephone;

	/**
	 * 公司详细地址
	 */
	@TableField("company_addr")
	private String companyAddr;

	/**
	 * 企业法人
	 */
	@TableField("enterprise_person")
	private String enterprisePerson;

	/**
	 * 公司地址所在省ID
	 */
	@TableField("province_id")
	private Long provinceId;

	/**
	 * 公司地址所在市ID
	 */
	@TableField("city_id")
	private Long cityId;

	/**
	 * 公司地址所在区ID
	 */
	@TableField("county_id")
	private Long countyId;

	/**
	 * 公司地址所在省名称
	 */
	@TableField("province")
	private String province;

	/**
	 * 公司地址所在市名称
	 */
	@TableField("city")
	private String city;

	/**
	 * 公司地址所在区名称
	 */
	@TableField("county")
	private String county;

	/**
	 * 开票信息开户银行
	 */
	@TableField("open_bank")
	private String openBank;

	/**
	 * 纳税人识别号
	 */
	@TableField("identification")
	private String identification;

	/**
	 * 地址电话
	 */
	@TableField("open_mobile")
	private String openMobile;

	/**
	 * 转账开户银行
	 */
	@TableField("transfer_bank")
	private String transferBank;

	/**
	 * 转账银行账号
	 */
	@TableField("transfer_account")
	private String transferAccount;
	/**
	 * 合作开始时间
	 */
	@TableField("cooperation_start_time")
	private Long cooperationStartTime;

	/**
	 * 合作结束时间
	 */
	@TableField("cooperation_end_time")
	private Long cooperationEndTime;

	/**
	 * 合作年限
	 */
	@TableField("years")
	private Double years;

	/**
	 * 营业执照
	 */
	@TableField("license")
	private String license;

	/**
	 * 经销合同
	 */
	@TableField("contract")
	private String contract;

	/**
	 * 授权书1
	 */
	@TableField("empower1")
	private String empower1;

	/**
	 * 授权书2
	 */
	@TableField("empower2")
	private String empower2;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
