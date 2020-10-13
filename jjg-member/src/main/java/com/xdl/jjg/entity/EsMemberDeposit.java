package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * <p>
 * 会员余额明细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_deposit")
public class EsMemberDeposit extends Model<EsMemberDeposit> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员ID
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 操作类型(0充值，1消费，2退款)
     */
	private String type;
    /**
     * 金额
     */
    @TableField("money")
    private Double money;
    /**
     * 操作时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 订单编号
     */
    @TableField("order_sn")
    private String orderSn;
    /**
     * 交易编号
     */
    @TableField("trade_sn")
    private String tradeSn;
    /**
     * 当前账户余额
     */
    @TableField("member_balance")
    private Double memberBalance;


}
