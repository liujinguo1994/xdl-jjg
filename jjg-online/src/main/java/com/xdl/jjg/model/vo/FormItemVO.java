package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FormItemVO
 * @Description: 付请求的表单项
 * @Author: libw  981087977@qq.com
 * @Date: 6/11/2019 15:37
 * @Version: 1.0
 */
@ApiModel
@Data
public class FormItemVO implements Serializable {

    @ApiModelProperty(value = "表单项名称")
    private String itemName;

    @ApiModelProperty(value = "表单项值")
    private String itemValue;
}
