package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * <p>
 * 店铺评分
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_shop_score")
public class EsMemberShopScore extends Model<EsMemberShopScore> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员id
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 订单编号
     */
    @TableField("order_sn")
	private String orderSn;
    /**
     * 发货速度评分
     */
    @TableField("delivery_score")
	private Double deliveryScore;
    /**
     * 描述相符度评分
     */
    @TableField("description_score")
	private Double descriptionScore;
    /**
     * 服务评分
     */
    @TableField("service_score")
	private Double serviceScore;
    /**
     * 卖家id
     */
    @TableField("shop_id")
	private Long shopId;

    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodId;
}
