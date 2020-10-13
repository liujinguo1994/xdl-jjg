package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 申请开店第三步
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-08
 */
@Data
@ToString
public class ApplyStep3DTO {
	/**银行开户名*/
	private String bankAccountName;
	/**银行开户账号*/
	private String bankNumber;
	/**开户银行支行名称*/
	private String bankName;
	/**开户银行所在省id*/
	private Long bankProvinceId;
	/**开户银行所在市id*/
	private Long bankCityId;
	/**开户银行所在县id*/
	private Long bankCountyId;
	/**开户银行所在镇id*/
	private Long bankTownId;
	/**开户银行所在省*/
	private String bankProvince;
	/**开户银行所在市*/
	private String bankCity;
	/**开户银行所在县*/
	private String bankCounty;
	/**开户银行所在镇*/
	private String bankTown;
	/**开户银行许可证电子版*/
	private String bankImg;
	/**税务登记证号*/
	private String taxesCertificateNum;
	/**纳税人识别号*/
	private String taxesDistinguishNum;
	/**税务登记证书*/
	private String taxesCertificateImg;
	/**申请开店进度*/
	private Integer step;

}

