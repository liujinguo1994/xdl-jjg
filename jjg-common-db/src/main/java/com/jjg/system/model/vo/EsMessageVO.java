package com.jjg.system.model.vo;

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
public class EsMessageVO implements Serializable {


    private static final long serialVersionUID = -6661909079448572469L;
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
    private Long adminId;
    /**
     * 管理员名称
     */
    private String adminName;
    /**
     * 发送时间
     */
    private Long sendTime;
    /**
     * 发送类型
     */
    private Integer sendType;

}
