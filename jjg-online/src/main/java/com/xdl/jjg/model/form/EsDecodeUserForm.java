package com.xdl.jjg.model.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * applet解密用户信息参数
 */
@Data
@ApiModel
public class EsDecodeUserForm implements Serializable {
    private static final long serialVersionUID = -1514943016630048932L;

    @ApiModelProperty(required = true,value = "临时登录凭证")
    @NotBlank(message = "临时登录凭证不能为空")
    private String code;
    @ApiModelProperty(required = true,value = "敏感数据")
    @NotBlank(message = "敏感数据不能为空")
    private String encrypteData;
    @ApiModelProperty(required = true,value = "解密算法的向量")
    @NotBlank(message = "解密算法的向量不能为空")
    private String iv;

}
