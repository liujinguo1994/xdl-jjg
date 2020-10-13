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
@TableName("es_express_platform")
public class EsExpressPlatform extends Model<EsExpressPlatform> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 快递平台名称
     */
    private String name;
    /**
     * 是否开启快递平台,1开启，0未开启
     */
    @TableField("is_open")
    private Integer isOpen;
    /**
     * 快递平台配置
     */
    private String config;
    /**
     * 快递平台beanid
     */
    private String bean;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
