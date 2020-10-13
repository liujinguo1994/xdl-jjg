package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 评价和收藏成长值配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-20 15:51:00
 */
@Data
@TableName("es_growth_value_strategy")
public class EsGrowthValueStrategy extends Model<EsGrowthValueStrategy> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 成长值类型1商品收藏，2店铺收藏，3评价
     */
    @TableField("growth_type")
    private Integer growthType;

    /**
     * 成长值
     */
    @TableField("growth_value")
    private Integer growthValue;
    /**
     * 每天限制次数
     */
    @TableField("limit_num")
    private Integer limitNum;
    /**
     * 开关 0打开，1关闭
     */
    @TableField("config_switch")
    private Integer configSwitch;

}
