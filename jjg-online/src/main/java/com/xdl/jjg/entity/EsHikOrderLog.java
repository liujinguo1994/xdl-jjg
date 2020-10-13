package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
@TableName("es_hik_order_log")
public class EsHikOrderLog extends Model<EsHikOrderLog> {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    /**
     * 海康操作人姓名
     */
    @TableField("name")
    private String name;

    /**
     * 创建时间（操作时间）
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 手机号码
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 退款单号
     */
    @TableField("refund_sn")
    private String refundSn;

    /**
     * 订单号
     */
    @TableField("order_sn")
    private String orderSn;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
