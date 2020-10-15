package com.jjg.system.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
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
    private String memberIds;

    /**
     * 管理员id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long adminId;

    /**
     * 管理员名称
     */
    private String adminName;

    /**
     * 发送时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sendTime;

    /**
     * 发送类型
     */
    private Integer sendType;
}
