package com.jjg.member.model.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询商品使用

 * @author yuanj
 * @version v1.0
 * @since v7.0.0
 * 2020年3月25日 上午10:55:08
 */
@ApiModel
@Data
public class SkuSpecValueVO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;
    @ApiModelProperty(value = "value")
    private String value;


}


