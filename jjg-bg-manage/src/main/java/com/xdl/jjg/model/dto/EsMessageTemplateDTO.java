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
public class EsMessageTemplateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模版编号
     */
    private String tplCode;

    /**
     * 模板名称
     */
    private String tplName;

    /**
     * 类型(会员 ,店铺 ,其他)
     */
    private String type;

    /**
     * 站内信提醒是否开启
     */
    private Integer noticeState;

    /**
     * 短信提醒是否开启
     */
    private Integer smsState;

    /**
     * 邮件提醒是否开启
     */
    private Integer emailState;

    /**
     * 站内信内容
     */
    private String content;

    /**
     * 短信内容
     */
    private String smsContent;

    /**
     * 邮件内容
     */
    private String emailContent;

    /**
     * 邮件标题
     */
    private String emailTitle;

}
