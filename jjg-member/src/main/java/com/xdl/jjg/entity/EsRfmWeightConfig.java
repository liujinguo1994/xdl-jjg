package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * RFM权重配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-07 15:50:05
 */
@Data
@TableName("es_rfm_weight_config")
public class EsRfmWeightConfig extends Model<EsRfmWeightConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
		private Long id;

	/**
	 * 类型 0:Recency,1:Frequency,2Monetary
	 */
	@TableField("type")
	private Integer type;

	/**
	 * 权重
	 */
	@TableField("weight")
	private Double weight;

	/**
	 * 状态0:正常，1删除
	 */
	@TableField("state")
	private Integer state;

}
