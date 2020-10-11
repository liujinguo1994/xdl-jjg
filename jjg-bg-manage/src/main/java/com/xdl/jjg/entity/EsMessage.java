package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
@TableName("es_message")
public class EsMessage extends Model<EsMessage> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 标题
     */
	private String title;
    /**
     * 消息内容
     */
	private String content;
    /**
     * 会员id
     */
    @TableField("member_ids")
	private String memberIds;
    /**
     * 管理员id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("admin_id")
	private Long adminId;
    /**
     * 管理员名称
     */
    @TableField("admin_name")
	private String adminName;
    /**
     * 发送时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("send_time")
	private Long sendTime;
    /**
     * 发送类型
     */
    @TableField("send_type")
	private Integer sendType;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
