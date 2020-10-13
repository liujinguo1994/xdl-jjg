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
@TableName("es_payment_method")
public class EsPaymentMethod extends Model<EsPaymentMethod> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 支付方式名称
     */
    @TableField("method_name")
    private String methodName;
    /**
     * 支付插件名称
     */
    @TableField("plugin_id")
    private String pluginId;
    /**
     * pc是否可用
     */
    @TableField("pc_config")
    private String pcConfig;
    /**
     * wap是否可用
     */
    @TableField("wap_config")
    private String wapConfig;
    /**
     * app 原生是否可用
     */
    @TableField("app_native_config")
    private String appNativeConfig;
    /**
     * 支付方式图片
     */
    private String image;
    /**
     * 是否支持原路退回
     */
    @TableField("is_retrace")
    private Integer isRetrace;
    /**
     * app RN是否可用
     */
    @TableField("app_react_config")
    private String appReactConfig;
    /**
     * applet是否可用
     */
    @TableField("applet_config")
    private String appletConfig;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
