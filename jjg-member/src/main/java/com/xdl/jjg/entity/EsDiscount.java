package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * <p>
 * 公司折扣表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_discount")
public class EsDiscount extends Model<EsDiscount> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 公司ID
     */
    @TableField("company_id")
	private Long companyId;
    /**
     * 商品分类ID
     */
    @TableField("category_id")
	private Long categoryId;

    /**
     * 商品分类名称
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 折扣（系数计算）
     */
	private Double discount;



}
