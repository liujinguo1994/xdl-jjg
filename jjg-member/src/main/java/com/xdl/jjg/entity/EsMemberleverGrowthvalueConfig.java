package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员等级成长值配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-18 15:10:57
 */
@Data
@TableName("es_memberlever_growthvalue_config")
public class EsMemberleverGrowthvalueConfig extends Model<EsMemberleverGrowthvalueConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 等级名称
	 */
	@TableField("level_name")
	private String levelName;

	/**
	 * 成长值下线
	 */
	@TableField("growth_value")
	private Integer growthValue;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

	/**
	 * 是否删除
	 */
	@TableField("is_del")
	@TableLogic(value="0",delval="1")
	private Integer isDel;

}
