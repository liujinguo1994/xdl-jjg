package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 运费模板详情表
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsFreightTemplateDetailDO extends Model<EsFreightTemplateDetailDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
	private Long id;

    /**
     * 关联模板id
     */
	private Long modeId;

    /**
     * 运送地区json
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
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 修改时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
	 * 运送地区
	 */
	private List<Long> areaId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
