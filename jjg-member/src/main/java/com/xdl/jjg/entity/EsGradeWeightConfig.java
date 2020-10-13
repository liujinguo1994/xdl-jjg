package com.xdl.jjg.entity;

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
 * @author LBW 981087977@qq.com
 * @since 2019-06-29 14:18:11
 */
@Data
@TableName("es_grade_weight_config")
public class EsGradeWeightConfig extends Model<EsGradeWeightConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id")
	private Long id;

	/**
	 * 评分名称
	 */
	@TableField("comment_name")
	private String commentName;

	/**
	 * 评分类型 0:商品评分,1物流评分,2服务评分
	 */
	@TableField("comment_type")
	private Integer commentType;

	/**
	 * 评分权重值
	 */
	@TableField("weight_value")
	private Double weightValue;

}
