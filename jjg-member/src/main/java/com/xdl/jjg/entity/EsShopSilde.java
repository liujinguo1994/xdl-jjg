package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 店铺幻灯片
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_shop_silde")
public class EsShopSilde extends Model<EsShopSilde> {

    private static final long serialVersionUID = 1L;

    /**
     * 幻灯片Id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 店铺Id
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 幻灯片URL
     */
    @TableField("silde_url")
	private String sildeUrl;
    /**
     * 图片URL
     */
	private String img;



}
