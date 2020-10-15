package com.jjg.shop.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺设置
 */
@Data
@ApiModel
public class ShopSettingForm implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;

    /**
     * 手机端背景图片
     */
    @ApiModelProperty(value = "手机端背景图片")
    private String backImage;

    /**
     * 店铺简介
     */
    @ApiModelProperty(value = "店铺简介")
    private String shopDesc;


}
