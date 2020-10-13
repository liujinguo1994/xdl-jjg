package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: AreaVO
 * @Description: 区域VO
 * @Author: libw  981087977@qq.com
 * @Date: 7/16/2019 20:01
 * @Version: 1.0
 */
@Data
@ApiModel(description = "区域")
public class AreaVO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "地区名称")
    private String localName;

    @ApiModelProperty(value = "父ID")
    private Long parentId;

    @ApiModelProperty(value = "级别")
    private Integer level;
}
