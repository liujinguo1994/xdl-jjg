package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统操作日志
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_sys_log")
public class EsSysLog extends Model<EsSysLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
    /**
     * 操作人ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("oper_id")
    private Long operId;
    /**
     * 操作内容
     */
    @TableField("oper_content")
    private String operContent;
    /**
     * 操作类型
     */
    @TableField("oper_type")
    private Integer operType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
