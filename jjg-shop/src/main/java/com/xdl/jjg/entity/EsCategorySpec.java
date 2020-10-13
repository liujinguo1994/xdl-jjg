package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_category_spec")
public class EsCategorySpec extends Model<EsCategorySpec> {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @TableField("category_id")
	private Long categoryId;
    /**
     * 规格id
     */
    @TableField("spec_id")
	private Long specId;


}
