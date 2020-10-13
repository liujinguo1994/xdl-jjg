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
 * @author LiuJG 344009799@qq.com
 * @since 2019-10-26
 */
@Data
@TableName("es_search_key_word")
public class EsSearchKeyWord extends Model<EsSearchKeyWord> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 搜索关键字
	 */
	@TableField("search_keyword")
	private String searchKeyword;

	/**
	 * 会员ID
	 */
	@TableField("member_id")
	private Long memberId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
