package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("es_specification")
public class EsSpecification extends Model<EsSpecification> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 规格项名称
     */
    @TableField("spec_name")
	private String specName;
    /**
     * 是否被删除0未 删除   1  删除
     */
    @TableField("is_del")
    @TableLogic(value = "0",delval ="1")
	private Integer isDel = 0;
    /**
     * 规格描述
     */
    @TableField("spec_memo")
	private String specMemo;
    /**
     * 所属卖家 0属于平台
     */

    @TableField("shop_id")
	private Long shopId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
