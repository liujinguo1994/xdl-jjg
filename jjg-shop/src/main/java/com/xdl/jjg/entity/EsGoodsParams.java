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
@TableName("es_goods_params")
public class EsGoodsParams extends Model<EsGoodsParams> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品id
     */

    @TableField("goods_id")
	private Long goodsId;
    /**
     * 参数id
     */

    @TableField("param_id")
	private Long paramId;
    /**
     * 参数名字
     */
    @TableField("param_name")
	private String paramName;
    /**
     * 参数值
     */
    @TableField("param_value")
	private String paramValue;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
