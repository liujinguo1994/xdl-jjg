package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 信任登录
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_connect")
public class EsConnect extends Model<EsConnect> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 会员ID
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 唯一标示union_id
     */
    @TableField("union_id")
    private String unionId;
    /**
     * 信任登录类型
     */
    @TableField("union_type")
    private String unionType;
    /**
     * 解绑时间
     */
    @TableField(value = "unbound_time", fill = FieldFill.INSERT_UPDATE)
    private Long unboundTime;
    /**
     * 解绑标识1删除0未删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField("is_del")
    private Integer isDel;


}
