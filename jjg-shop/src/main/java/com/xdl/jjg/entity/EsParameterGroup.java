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
@TableName("es_parameter_group")
public class EsParameterGroup extends Model<EsParameterGroup> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 参数组名称
     */
    @TableField("group_name")
	private String groupName;
    /**
     * 关联分类id
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
