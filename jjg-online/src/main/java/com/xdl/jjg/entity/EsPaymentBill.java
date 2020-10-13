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
 * 会员支付帐单-es_payment_bill
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_payment_bill")
public class EsPaymentBill extends Model<EsPaymentBill> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 订单编号
     */
    @TableField("order_sn")
    private String orderSn;
    /**
     * 提交给第三方平台单号
     */
    @TableField("out_trade_no")
    private String outTradeNo;
    /**
     * 第三方平台返回交易号
     */
    @TableField("return_trade_no")
    private String returnTradeNo;
    /**
     * 是否已支付
     */
    @TableField("is_pay")
    private Integer isPay;
    /**
     * 交易类型(交易；订单)
     */
    @TableField("trade_type")
    private String tradeType;
    /**
     * 支付方式名称
     */
    @TableField("payment_name")
    private String paymentName;
    /**
     * 支付参数(第三方配置信息)
     */
    @TableField("pay_config")
    private String payConfig;
    /**
     * 交易金额
     */
    @TableField("trade_money")
    private Double tradeMoney;
    /**
     * 支付插件id
     */
    @TableField("payment_plugin_id")
    private String paymentPluginId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
