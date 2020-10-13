package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 自定义分词
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-01 13:54:19
 */
@Data
@TableName("es_goods_words")
public class EsGoodsWords extends Model<EsGoodsWords> {

	/**
	 * 分词名称
	 */
	@TableField("words")
	private String words;

	/**
	 * 商品数量
	 */
	@TableField("goods_num")
	private Long goodsNum;

	/**
	 * 全拼
	 */
	@TableField("quanpin")
	private String quanpin;

	/**
	 * 首字母
	 */
	@TableField("szm")
	private String szm;

}
