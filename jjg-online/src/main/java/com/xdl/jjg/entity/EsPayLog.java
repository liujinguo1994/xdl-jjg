package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 支付记录日志表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_pay_log")
public class EsPayLog extends Model<EsPayLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 订单编号
     */
    @TableField("order_sn")
    private String orderSn;
    /**
     * 支付类型(支付宝、微信、银联)
     */
    @TableField("pay_type")
    private String payType;
    /**
     * 付款时间
     */
    @TableField("pay_time")
    private Long payTime;
    /**
     * 付款金额
     */
    @TableField("pay_money")
    private BigDecimal payMoney;
    /**
     * 付款会员名
     */
    @TableField("pay_member_name")
    private String payMemberName;
    /**
     * 付款状态
     */
    @TableField("pay_state")
    private String payState;
    /**
     * 收款单流水号
     */
    @TableField("pay_log_sn")
    private String payLogSn;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
