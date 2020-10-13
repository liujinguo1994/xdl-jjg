package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * <p>
 * 会员优惠券
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_coupon")
public class EsMemberCoupon extends Model<EsMemberCoupon> {

    private static final long serialVersionUID = 1L;

    /**
     * 消费类型
     */
    @TableField("coupon_type")
    private String couponType;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 会员ID
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 优惠券表主键 (es_coupon)
     */
    @TableField("coupon_id")
    private Long couponId;
    /**
     * 使用时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
    /**
     * 领取时间
     */
    @TableField("create_time")
    private Long createTime;
    /**
     * 订单主键
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 订单编号
     */
    @TableField("order_sn")
    private String orderSn;
    /**
     * 会员昵称
     */
    @TableField("member_name")
    private String memberName;
    /**
     * 优惠券名称
     */
    private String title;
    /**
     * 优惠券面额
     */
    @TableField("coupon_money")
    private Double couponMoney;
    /**
     * 优惠券门槛金额
     */
    @TableField("coupon_threshold_price")
    private Double couponThresholdPrice;
    /**
     * 使用起始时间
     */
    @TableField("start_time")
    private Long startTime;
    /**
     * 使用截止时间
     */
    @TableField("end_time")
    private Long endTime;
    /**
     * 使用状态 1:未使用, 2:已使用,3失效
     */
    private Integer state;

    @TableField("is_check")
    private Integer isCheck;
    /**
     * 商家ID,优惠券属于哪个商家
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 商家店名
     */
    @TableField("shop_name")
    private String shopName;

    @TableField("is_del")
    @TableLogic(value = "1",delval = "2")
    private Integer isDel;


}
