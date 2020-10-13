package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * <p>
 * 店铺模板详情--es_template_detail
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_template_detail")
public class EsTemplateDetail extends Model<EsTemplateDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 模板类型
     */
	private Integer type;
    /**
     * 模板名称
     */
	private String name;
    /**
     * 位置
     */
	private Integer location;
    /**
     * 修改时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Long updateTime;
    /**
     * 模板规格
     */
    @TableField("temp_size")
	private Integer tempSize;
    /**
     * 应用商品
     */
    @TableField("hot_sell_goods")
	private String hotSellGoods;
    /**
     * 应用分类
     */
    @TableField("category_for_tems")
    @JsonIgnore
	private String categoryForTems;
    /**
     * 客户端类型
     */
    @TableField("client_type")
    @JsonIgnore
	private Integer clientType;


}
