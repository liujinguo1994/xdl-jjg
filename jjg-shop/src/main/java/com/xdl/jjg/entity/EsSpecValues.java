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
@TableName("es_spec_values")
public class EsSpecValues extends Model<EsSpecValues> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 规格项id
     */

    @TableField("spec_id")
	private Long specId;
    /**
     * 规格值名字
     */
    @TableField("spec_value")
	private String specValue;
    /**
     * 所属卖家
     */

    @TableField("shop_id")
	private Long shopId;
    /**
     * 规格名称
     */
    @TableField("spec_name")
	private String specName;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
