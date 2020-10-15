package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 站内消息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsMessageForm implements Serializable {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", required = true)
    @NotBlank(message = "标题不能为空")
    private String title;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank(message = "站内消息内容不能为空")
    private String content;
    /**
     * 发送类型
     */
    @ApiModelProperty(value = "发送类型(0全站，1指定会员)", required = true, example = "1")
    @NotNull(message = "发送类型不能为空")
    private Integer sendType;
    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String memberIds;

}
