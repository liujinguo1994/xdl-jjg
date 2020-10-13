package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 运费模板表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_ship_template")
public class EsShipTemplate extends Model<EsShipTemplate> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 模板名称
     */
    @TableField("mode_name")
    private String modeName;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
    /**
     * 是否删除(0 不删除，1删除)
     */
    @TableField("is_del")
    private Integer isDel;
    /**
     * 是否生鲜（0生鲜，1非生鲜）
     */
    @TableField("is_fresh")
    private Integer isFresh;
    /**
     * 店铺id
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 物流公司ID
     */
    @TableField("logi_id")
    private Long logiId;
    /**
     * 物流公司名称
     */
    @TableField("logi_name")
    private String logiName;
    /**
     * 是否是活动(0是活动，1不是活动)
     */
    private Integer sign;
    @TableField("is_ng")
    private Long isNg;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}