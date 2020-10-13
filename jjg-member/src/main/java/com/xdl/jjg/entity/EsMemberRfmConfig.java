package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * <p>
 * rfm配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:54
 */
@Data
@TableName("es_member_rfm_config")
public class EsMemberRfmConfig extends Model<EsMemberRfmConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 消费间隔
	 */
	@TableField("recency")
	private Integer recency;

	/**
	 * 消费间隔成长值
	 */
	@TableField("recency_growth_value")
	private Integer recencyGrowthValue;

	/**
	 * 消费频率
	 */
	@TableField("frequency")
	private Integer frequency;

	/**
	 * 消费频率成长值
	 */
	@TableField("frequency_growth_value")
	private Integer frequencyGrowthValue;

	/**
	 * 消费金额
	 */
	@TableField("monetary")
	private Double monetary;

	/**
	 * 消费金额成长值
	 */
	@TableField("monetary_growth_value")
	private Integer monetaryGrowthValue;

	/**
	 * rfm信息json字符串
	 */
	@TableField("rfm_info")
	private String rfmInfo;

}
