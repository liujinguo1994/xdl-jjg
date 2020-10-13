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
@TableName("es_goods_gallery")
public class EsGoodsGallery extends Model<EsGoodsGallery> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * skuID
     */

    @TableField("sku_id")
	private Long skuId;
    /**
     * 缩略图路径
     */
	private String image;
    /**
     * 排序
     */
	private Integer sort;
	@TableField("album_no")
	private String albumNo;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
