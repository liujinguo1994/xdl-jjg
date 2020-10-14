package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 运费模板详情表
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
public class EsFreightTemplateDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
	private Long id;

    /**
     * 关联模板id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long modeId;

    /**
     * 运送地区
     */
	private String area;

    /**
     * 首重
     */
	private Double firstWeight;

    /**
     * 首费
     */
	private Double firstTip;

    /**
     * 续重
     */
	private Double sequelWeight;

    /**
     * 续费
     */
	private Double sequelTip;

    /**
     * 是否删除(0否,1删除)
     */
	private Integer isDel;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 修改时间
     */
	private Long updateTime;

    /**
     * 实付金额最小值
     */
	private Double minMoney;

    /**
     * 实付金额最大值
     */
	private Double maxMoney;

	/**
	 * 地区ID
	 */
	private String areaId;
	/**
	 * 地区Json
	 */
	private String areaJson;


	protected Serializable pkVal() {
		return this.id;
	}

}
