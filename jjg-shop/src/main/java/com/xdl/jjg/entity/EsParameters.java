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
@TableName("es_parameters")
public class EsParameters extends Model<EsParameters> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 参数名称
     */
    @TableField("param_name")
	private String paramName;
    /**
     * 参数类型1 输入项   2 选择项
     */
    @TableField("param_type")
	private Integer paramType;
    /**
     * 选择值，当参数类型是选择项2时，必填，逗号分隔
     */
	private String options;
    /**
     * 是否可索引，0 不显示 1 显示
     */
    @TableField("is_index")
	private Integer isIndex;
    /**
     * 是否必填是  1    否   0
     */
	private Integer required;
    /**
     * 参数分组id
     */

    @TableField("group_id")
	private Long groupId;
    /**
     * 商品分类id
     */

    @TableField("category_id")
	private Long categoryId;
    /**
     * 排序
     */
	private Integer sort;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
