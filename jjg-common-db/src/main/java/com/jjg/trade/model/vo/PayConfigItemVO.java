package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: PayConfigItemVO
 * @Description: 配置文件映射实体
 * @Author: libw  981087977@qq.com
 * @Date: 6/3/2019 19:53
 * @Version: 1.0
 */
@Data
@ApiModel
public class PayConfigItemVO implements Serializable {

    /**
     * 配置文件name值
     */
    @ApiModelProperty(name = "配置参数name值，以后台返回为准")
    private String name;

    /**
     * 配置文件name映射文本值
     */
    @ApiModelProperty(hidden = true)
    private String text;

    /**
     * 配置的值
     */
    @ApiModelProperty(name = "配置参数值，界面添加为准")
    private String value;
}
