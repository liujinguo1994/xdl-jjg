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
 *
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_order_log")
public class EsOrderLog extends Model<EsOrderLog> {

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
     * 操作者id
     */
    @TableField("op_id")
    private Long opId;
    /**
     * 操作者名称
     */
    @TableField("op_name")
    private String opName;
    /**
     * 日志信息
     */
    private String message;
    /**
     * 操作时间
     */
    @TableField("op_time")
    private Long opTime;
    /**
     * 订单金额
     */
    private Double money;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
