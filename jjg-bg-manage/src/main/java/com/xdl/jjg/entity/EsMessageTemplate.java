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
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_message_template")
public class EsMessageTemplate extends Model<EsMessageTemplate> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 模版编号
     */
    @TableField("tpl_code")
    private String tplCode;
    /**
     * 模板名称
     */
    @TableField("tpl_name")
    private String tplName;
    /**
     * 类型(会员 ,店铺 ,其他)
     */
    private String type;
    /**
     * 站内信提醒是否开启
     */
    @TableField("notice_state")
    private Integer noticeState;
    /**
     * 短信提醒是否开启
     */
    @TableField("sms_state")
    private Integer smsState;
    /**
     * 邮件提醒是否开启
     */
    @TableField("email_state")
    private Integer emailState;
    /**
     * 站内信内容
     */
    private String content;
    /**
     * 短信内容
     */
    @TableField("sms_content")
    private String smsContent;
    /**
     * 邮件内容
     */
    @TableField("email_content")
    private String emailContent;
    /**
     * 邮件标题
     */
    @TableField("email_title")
    private String emailTitle;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
