package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 店铺模版
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_shop_themes")
public class EsShopThemes extends Model<EsShopThemes> {

    private static final long serialVersionUID = 1L;

    /**
     * 模版id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 模版名称
     */
	private String name;
    /**
     * 图片模板路径
     */
    @TableField("image_path")
	private String imagePath;
    /**
     * 是否为默认,1是默认
     */
    @TableField("is_default")
	private Integer isDefault;
    /**
     * 模版类型
     */
	private String type;
    /**
     * 模版标识
     */
	private String mark;


}
