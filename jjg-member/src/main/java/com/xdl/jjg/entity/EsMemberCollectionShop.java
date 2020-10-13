package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员收藏店铺表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_collection_shop")
public class EsMemberCollectionShop extends Model<EsMemberCollectionShop> {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员id
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 店铺id
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 店铺名称
     */
    @TableField("shop_name")
	private String shopName;
    /**
     * 收藏时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 店铺logo
     */
	private String logo;
    /**
     * 店铺所在省
     */
    @TableField("shop_province")
	private String shopProvince;
    /**
     * 店铺所在市
     */
    @TableField("shop_city")
	private String shopCity;
    /**
     * 店铺所在县
     */
    @TableField("shop_region")
	private String shopRegion;
    /**
     * 店铺所在镇
     */
    @TableField("shop_town")
	private String shopTown;
    /**
     * 收藏店铺的排序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * 店铺备注
     */
    @TableField("mark")
    private String mark;



}
