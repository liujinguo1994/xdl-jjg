package com.xdl.jjg.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 申请开店第四步
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-08
 */
@Data
@ToString
public class ApplyStep4VO {
	/**店铺名称*/
	private String shopName;
	/**店铺经营类目*/
	private String goodsManagementCategory;
	/**店铺所在省id*/
	private Long shopProvinceId;
	/**店铺所在市id*/
	private Long shopCityId;
	/**店铺所在县id*/
	private Long shopCountyId;
	/**店铺所在镇id*/
	private Long shopTownId;
	/**店铺所在省*/
	private String shopProvince;
	/**店铺所在市*/
	private String shopCity;
	/**店铺所在县*/
	private String shopCounty;
	/**店铺所在镇*/
	private String shopTown;
	/**申请开店进度*/
	private Integer step;



}