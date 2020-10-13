package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_seckill_range")
public class EsSeckillRange extends Model<EsSeckillRange> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 限时抢购活动id
	 */
	@TableField("seckill_id")
	private Long seckillId;
	/**
	 * 整点时刻
	 */
	@TableField("range_time")
	private Integer rangeTime;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
