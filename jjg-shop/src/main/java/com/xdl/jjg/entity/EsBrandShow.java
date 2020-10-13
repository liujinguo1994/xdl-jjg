package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-12 13:23:46
 */
@Data
@Accessors(chain = true)
@TableName("es_brand_show")
public class EsBrandShow extends Model<EsBrandShow> {

	private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;

	@TableField("name")
	private String name;

	@TableField("img_url")
	private String imgUrl;

	@TableField("link_url")
	private String linkUrl;

	@TableField("sort")
	private Integer sort;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
