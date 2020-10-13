package com.xdl.jjg.entity;


import com.baomidou.mybatisplus.annotation.*;
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
@TableName("es_brand")
public class EsBrand extends Model<EsBrand> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 品牌名称
     */
	private String name;
    /**
     * 品牌图标
     */
	private String logo;
    /**
     * 是否删除，0删除1未删除
     */
	@TableLogic(value="0",delval="1")
    @TableField(value = "is_del")
	private Integer isDel;


}
