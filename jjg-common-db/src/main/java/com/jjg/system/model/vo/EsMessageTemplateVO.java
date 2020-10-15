package com.jjg.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 消息模板vo
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsMessageTemplateVO implements Serializable {

    private static final long serialVersionUID = 5987063280410489728L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 模版编号
     */
    @ApiModelProperty(value = "模版编号")
    private String tplCode;
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String tplName;
    /**
     * 类型(会员 ,店铺 )
     */
    @ApiModelProperty(value = "类型(MEMBER:会员 ,SHOP:店铺)")
    private String type;
    /**
     * 站内信提醒是否开启(1:开启,2:关闭)
     */
    @ApiModelProperty(value = "站内信提醒是否开启(1:开启,2:关闭)", example = "1")
    private Integer noticeState;
    /**
     * 短信提醒是否开启(1:开启,2:关闭)
     */
    @ApiModelProperty(value = "短信提醒是否开启(1:开启,2:关闭)", example = "1")
    private Integer smsState;
    /**
     * 邮件提醒是否开启(1:开启,2:关闭)
     */
    @ApiModelProperty(value = "邮件提醒是否开启(1:开启,2:关闭)", example = "1")
    private Integer emailState;
    /**
     * 站内信内容
     */
    @ApiModelProperty(value = "站内信内容")
    private String content;
    /**
     * 短信内容
     */
    @ApiModelProperty(value = "短信内容")
    private String smsContent;
    /**
     * 邮件内容
     */
    @ApiModelProperty(value = "邮件内容")
    private String emailContent;
    /**
     * 邮件标题
     */
    @ApiModelProperty(value = "邮件标题")
    private String emailTitle;
}
