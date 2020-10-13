package com.xdl.jjg.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 运费模板表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsShipTemplateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
	private Long id;
    /**
     * 模板名称
     */
	private String modeName;
    /**
     * 创建时间
     */
	private Long createTime;
    /**
     * 修改时间
     */
	private Long updateTime;
    /**
     * 是否删除(0 不删除，1删除)
     */
	private Integer isDel;
    /**
     * 是否生鲜（1生鲜，2非生鲜）
     */
	private Integer isFresh;
    /**
     * 店铺id
     */
	private Long shopId;
    /**
     * 物流公司ID
     */
	private Long logiId;
    /**
     * 物流公司名称
     */
	private String logiName;
    /**
     * 是否是活动(0是活动，1不是活动)
     */
	private Integer sign;

	@ApiModelProperty(value = "运费模板详情集合")
	private List<EsFreightTemplateDetailDTO> freightDetailList;


	private List<EsShipCompanyDetailsDTO> esShipCompanyDetailsList;

    private List<Integer> modeId;

	private Long isNg;
	protected Serializable pkVal() {
		return this.id;
	}

}
