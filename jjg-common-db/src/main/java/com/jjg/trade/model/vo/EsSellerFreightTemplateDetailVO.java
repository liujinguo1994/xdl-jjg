package com.jjg.trade.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 卖家端运费模板详情表
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSellerFreightTemplateDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    @ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 关联模板id
     */
	@ApiModelProperty(value = "关联模板id")
	private Long modeId;

    /**
     * 运送地区json
     */
	@ApiModelProperty(value = "运送地区json")
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
	@ApiModelProperty(value = "地区ID")
	private String areaId;
	/**
	 * 地区Json
	 */
	@ApiModelProperty(value = "地区Json")
	private String areaJson;
	private Integer isFresh;

}
