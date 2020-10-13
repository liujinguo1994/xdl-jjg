package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 店铺表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_shop")
public class EsShop extends Model<EsShop> {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员ID
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 会员名称
     */
    @TableField("member_name")
	private String memberName;
    /**
     * 店铺状态
     */
	private String state;
    /**
     * 店铺名称
     */
    @TableField("shop_name")
	private String shopName;
    /**
     * 店铺创建时间
     */
    @TableField("shop_createtime")
	private Long shopCreatetime;
    /**
     * 店铺关闭时间
     */
    @TableField("shop_endtime")
	private Long shopEndtime;

    /**
     * 佣金比列
     */
    @TableField("commission")
    private Double commission;


}
