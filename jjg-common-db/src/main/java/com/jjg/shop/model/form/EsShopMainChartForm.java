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
public class EsShopMainChartForm implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺横幅")
    private String shopBanner;

    /**
     * 手机端背景图片
     */
    @ApiModelProperty(value = "店铺横幅跳转链接")
    private String bannerUrl;




}
