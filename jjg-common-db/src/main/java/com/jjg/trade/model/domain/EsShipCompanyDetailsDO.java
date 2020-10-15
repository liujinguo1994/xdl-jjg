package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 公司运费模版详情
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-11-28 10:41:09
 */
@Data
public class EsShipCompanyDetailsDO extends Model<EsShipCompanyDetailsDO> {

    private static final long serialVersionUID = 1L;

	private Long id;

	private String areaId;

	private Long modeId;

	private Long shopId;

    /**
     * 1：非生鲜 2：生鲜
     */
	private Integer type;

	private Double lowerPrice;

	private Double highPrice;

	private Double firstCompany;

	private Double firstPrice;

	private Double continuedCompany;

	private Double continuedPrice;

	private Long isNg;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
