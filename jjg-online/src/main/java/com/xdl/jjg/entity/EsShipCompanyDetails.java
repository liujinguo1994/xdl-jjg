package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 公司运费模版详情
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-28 10:41:07
 */
@Data
@TableName("es_ship_company_details")
public class EsShipCompanyDetails extends Model<EsShipCompanyDetails> {

	private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	@TableField("area_id")
	private Long areaId;

	/**
	 * 关联模板id
	 */
	@TableField("mode_id")
	private Long modeId;
	/**
	 * 店铺ID
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 2：非生鲜 1：生鲜
	 */
	@TableField("type")
	private Integer type;

	@TableField("lower_price")
	private Double lowerPrice;

	@TableField("high_price")
	private Double highPrice;

	@TableField("first_company")
	private Double firstCompany;

	@TableField("first_price")
	private Double firstPrice;

	@TableField("continued_company")
	private Double continuedCompany;

	@TableField("continued_price")
	private Double continuedPrice;
	@TableField("is_ng")
	private Long isNg;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
