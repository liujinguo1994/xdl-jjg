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
 * 订单拓展信息表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_order_meta")
public class EsOrderMeta extends Model<EsOrderMeta> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type= IdType.AUTO)
	private Long id;
	/**
	 * 订单编号
	 */
	@TableField("order_sn")
	private String orderSn;
	/**
	 * 扩展-键
	 */
	@TableField("meta_key")
	private String metaKey;
	/**
	 * 扩展-值
	 */
	@TableField("meta_value")
	private String metaValue;
	/**
	 * 售后状态
	 */
	private String state;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
