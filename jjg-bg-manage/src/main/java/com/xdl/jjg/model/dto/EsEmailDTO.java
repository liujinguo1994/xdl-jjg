package com.xdl.jjg.model.dto;

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
public class EsEmailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 邮件类型
     */
    private String type;

    /**
     * 是否成功
     */
    private Integer isSuccess;

    /**
     * 邮件接收者
     */
    private String email;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 错误次数
     */
    private Integer errorNum;

    /**
     * 最后发送时间
     */
    private Long updateTime;


}
