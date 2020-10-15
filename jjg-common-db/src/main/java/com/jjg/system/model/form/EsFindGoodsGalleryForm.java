package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 发现好货相册
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Data
@ApiModel
public class EsFindGoodsGalleryForm implements Serializable {

    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径")
    private String url;
}
