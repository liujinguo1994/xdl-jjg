package com.jjg.member.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class EsShipCompanyDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;

	private String areaId;

	private Long shopId;

    /**
     * 2：非生鲜 1：生鲜
     */
	private Integer type;

	private Double lowerPrice;

	private Double highPrice;

	private Double firstCompany;

	private Double firstPrice;

	private Double continuedCompany;

	private Double continuedPrice;

	private Long isNg;
	protected Serializable pkVal() {
		return this.id;
	}

}
