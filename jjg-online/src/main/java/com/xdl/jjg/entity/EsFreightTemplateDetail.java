package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("es_freight_template_detail")
public class EsFreightTemplateDetail extends Model<EsFreightTemplateDetail> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键自增
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 关联模板id
	 */
	@TableField("mode_id")
	private Long modeId;

	/**
	 * 运送地区
	 */
	@TableField("area")
	private String area;

	/**
	 * 首重
	 */
	@TableField("first_weight")
	private Double firstWeight;

	/**
	 * 首费
	 */
	@TableField("first_tip")
	private Double firstTip;

	/**
	 * 续重
	 */
	@TableField("sequel_weight")
	private Double sequelWeight;

	/**
	 * 续费
	 */
	@TableField("sequel_tip")
	private Double sequelTip;

	/**
	 * 是否删除(0否,1删除)
	 */
	@TableField("is_del")
	private Integer isDel;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	/**
	 * 修改时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	/**
	 * 实付金额最小值
	 */
	@TableField("min_money")
	private Double minMoney;

	/**
	 * 实付金额最大值
	 */
	@TableField("max_money")
	private Double maxMoney;

	/**
	 * 地区ID
	 */
	@TableField("area_id")
	private String areaId;
	/**
	 * 地区Json
	 */
	@TableField("area_json")
	private String areaJson;

	@TableField("is_fresh")
	private Integer isFresh;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
