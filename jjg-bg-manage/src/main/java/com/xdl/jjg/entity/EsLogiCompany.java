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
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_logi_company")
public class EsLogiCompany extends Model<EsLogiCompany> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 物流公司名称
     */
    private String name;
    /**
     * 物流公司code
     */
    private String code;
    /**
     * 快递鸟物流公司code
     */
    private String kdcode;
    /**
     * 是否支持电子面单1：支持 0：不支持
     */
    @TableField("is_waybill")
    private Integer isWaybill;
    /**
     * 物流公司客户号
     */
    @TableField("customer_name")
    private String customerName;
    /**
     * 物流公司电子面单密码
     */
    @TableField("customer_pwd")
    private String customerPwd;

    /**
     * 是否有效 0 有效 1 无效
     */
    @TableField("state")
    private Integer state;

    @TableField("phone")
    private String phone;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
