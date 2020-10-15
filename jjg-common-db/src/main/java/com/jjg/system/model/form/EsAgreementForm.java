package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;


/**
 * <p>
 * 协议维护
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsAgreementForm implements Serializable {
    private static final long serialVersionUID = 3809591295032230596L;
    /**
     * 协议内容
     */
    @ApiModelProperty(required = true, value = "协议内容")
    @NotBlank(message = "协议内容不能为空")
    private String content;

}
