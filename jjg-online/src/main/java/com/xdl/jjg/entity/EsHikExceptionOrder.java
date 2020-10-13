package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *  异常订单表实体（海康用）
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-05-09
 */
@Data
@Accessors(chain = true)
@TableName("es_exception_order")
public class EsHikExceptionOrder extends Model<EsHikExceptionOrder>{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;


    /**
     * 联系人姓名
     */
    @TableField("name")
    private String name;

    /**
     * 创建时间（操作时间）
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Long updateTime;

    /**
     * 手机号码
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 订单号
     */
    @TableField("order_sn")
    private String orderSn;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
