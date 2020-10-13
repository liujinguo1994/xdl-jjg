package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 分类运费模板关联表
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@TableName("es_goods_freight")
public class EsGoodsFreight extends Model<EsGoodsFreight> {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * 运费模板id
	 */
	@TableField("mode_id")
	private Long modeId;

	/**
	 * 商品分类id
	 */
	@TableField("category_id")
	private Long categoryId;

	/**
	 * 是否删除(0 不 删除,1删除)
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
