package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-08 16:20:28
 */
@Data
@TableName("es_growth_weight_config")
public class EsGrowthWeightConfig extends Model<EsGrowthWeightConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 类型 0:RFM策略,1:评价模型策略,2收藏模型策略
	 */
	@TableField("type")
	private Integer type;

	/**
	 * 权重
	 */
	@TableField("weight")
	private Double weight;

	/**
	 * 状态1:正常，2删除
	 */
	@TableField("state")
	private Integer state;

}
