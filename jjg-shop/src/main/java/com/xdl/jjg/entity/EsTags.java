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
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_tags")
public class EsTags extends Model<EsTags> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 标签名字
     */
    @TableField("tag_name")
	private String tagName;
    /**
     * 所属卖家
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 关键字
     */
	private String mark;

	/**
	 * 排序
	 */
	private Integer sort;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
