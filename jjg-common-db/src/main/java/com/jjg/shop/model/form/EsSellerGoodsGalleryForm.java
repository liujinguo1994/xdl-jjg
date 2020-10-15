package com.jjg.shop.model.form;
/**
 * @author wangaf
 * @date 2019/12/6 10:00
 **/

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/12/6
 @Version V1.0
 **/
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSellerGoodsGalleryForm implements Serializable {
    /**
     * skuID
     */
    @ApiModelProperty(value = "skuID")
    private Long skuId;

    /**
     * 缩略图路径
     */
    @ApiModelProperty(value = "缩略图路径")
    @NotBlank(message = "缩略图路径不能为空")
    private String image;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
}
