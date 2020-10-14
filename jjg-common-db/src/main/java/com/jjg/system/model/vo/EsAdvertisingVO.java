package com.jjg.member.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 广告位
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Data
@ApiModel
public class EsAdvertisingVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 图片名称
     */
    @ApiModelProperty(value = "图片名称")
    private String picName;

    /**
     * 位置(首页，常买清单)
     */
    @ApiModelProperty(value = "位置(首页，常买清单)")
    private String location;

    /**
     * 连接
     */
    @ApiModelProperty(value = "连接")
    private String link;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String picUrl;

}
