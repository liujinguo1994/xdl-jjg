package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 发现好货
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Data
@TableName("es_find_goods")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsFindGoods extends Model<EsFindGoods> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 商品名称
	 */
	@TableField("goods_name")
	private String goodsName;

	/**
	 * 自定义分类id
	 */
	@TableField("custom_category_id")
	private Long customCategoryId;

	/**
	 * 商品链接
	 */
	@TableField("goods_url")
	private String goodsUrl;

	/**
	 * 图片地址
	 */
	@TableField("pic_url")
	private String picUrl;

	/**
	 * 商品id
	 */
	@TableField("goods_id")
	private Long goodsId;

	/**
	 * 商品描述
	 */
	@TableField("goods_description")
	private String goodsDescription;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	/**
	 * 最后修改时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
