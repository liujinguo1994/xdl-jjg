package com.jjg.trade.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 运费模板详情表VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsFreightTemplateDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     * @author: libw 981087977@qq.com
     * @date: 2019/07/03 14:24:47
     */
    public EsFreightTemplateDetailVO(){
    	this.firstTip = 0.0;
    	this.firstWeight = 0.0;
    	this.sequelTip = 0.0;
    	this.sequelWeight = 0.0;
	}

    /**
     * 主键自增
     */
	@ApiModelProperty(value = "主键自增")
	private Long id;

    /**
     * 关联模板id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "关联模板id")
	private Long modeId;

    /**
     * 运送地区
     */
	@ApiModelProperty(value = "运送地区")
	private String area;

    /**
     * 首重
     */
	@ApiModelProperty(value = "首重")
	private Double firstWeight;

    /**
     * 首费
     */
	@ApiModelProperty(value = "首费")
	private Double firstTip;

    /**
     * 续重
     */
	@ApiModelProperty(value = "续重")
	private Double sequelWeight;

    /**
     * 续费
     */
	@ApiModelProperty(value = "续费")
	private Double sequelTip;

    /**
     * 是否删除(0否,1删除)
     */
	@ApiModelProperty(value = "是否删除(0否,1删除)")
	private Integer isDel;

    /**
     * 创建时间
     */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

    /**
     * 修改时间
     */
	@ApiModelProperty(value = "修改时间")
	private Long updateTime;

    /**
     * 实付金额最小值
     */
	@ApiModelProperty(value = "实付金额最小值")
	private Double minMoney;

    /**
     * 实付金额最大值
     */
	@ApiModelProperty(value = "实付金额最大值")
	private Double maxMoney;

	/**
	 * 地区ID
	 */
	@ApiModelProperty(value = " 地区ID")
	private String areaId;
	/**
	 * 地区Json
	 */
	@ApiModelProperty(value = " 地区Json")
	private String areaJson;

	protected Serializable pkVal() {
		return this.id;
	}

}
