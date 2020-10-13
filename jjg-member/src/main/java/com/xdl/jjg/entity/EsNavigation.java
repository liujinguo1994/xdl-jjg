package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 店铺导航管理
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-05
 */
@Data
@TableName("es_navigation")
public class EsNavigation extends Model<EsNavigation> {

    private static final long serialVersionUID = 1L;

    /**
     * 导航id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 名称
     */
	private String name;
    /**
     * 是否显示
     */
	private Integer isDel;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 导航内容
     */
	private String contents;
    /**
     * URL
     */
    @TableField("nav_url")
	private String navUrl;
    /**
     * 新窗口打开
     */
	private String target;
    /**
     * 店铺id
     */
    @TableField("shop_id")
	private Long shopId;



}
