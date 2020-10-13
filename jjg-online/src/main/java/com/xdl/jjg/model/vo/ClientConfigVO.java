package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author fk
 * @version v2.0
 * @Description: 客户端配置
 * @date 2018/4/1117:05
 * @since v7.0.0
 */
@Data
@ApiModel
public class ClientConfigVO implements Serializable{

    @ApiModelProperty(name = "字段name")
    @NotEmpty(message = "客户端key不能为空")
    private String key;

    @ApiModelProperty(name = "字段文本提示")
    private String name;

    @ApiModelProperty(name = "字段文本提示",value = "config_list")
    private List<PayConfigItemVO>  configList;

    @ApiModelProperty(name = "是否开启 1开启 0关闭",value = "is_open")
    @NotNull(message = "是否开启某客户端不能为空")
    @Min(value = 0,message = "是否开启某客户端值不正确")
    @Max(value = 1,message = "是否开启某客户端值不正确")
    private Integer isOpen;
}
