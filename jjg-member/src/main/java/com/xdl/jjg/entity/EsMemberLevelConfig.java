package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员配置
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-04 09:42:02
 */
@Data
@TableName("es_member_level_config")
public class EsMemberLevelConfig extends Model<EsMemberLevelConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 等级名称
	 */
	@TableField("level")
	private String level;

	/**
	 * 成长值下线
	 */
	@TableField("under_line")
	private Integer underLine;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;

	/**
	 * 修改时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;

}
