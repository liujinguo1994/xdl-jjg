package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fk
 * @version v1.0
 * @Description: 商家查询商品使用
 * @date 2018/5/21 14:36
 * @since v7.0.0
 */
@ApiModel
@Data
public class LfcItemDetailVO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;

    @ApiModelProperty(name = "detail_html", value = "sku图片")
    private String detailHtml;

    @ApiModelProperty(name = "pic_url1", value = "商品图片1")
    private String picUrl1;
    @ApiModelProperty(name = "pic_url2", value = "商品图片2")
    private String picUrl2;
    @ApiModelProperty(name = "pic_url3", value = "商品图片3")
    private String picUrl3;
    @ApiModelProperty(name = "pic_url4", value = "商品图片4")
    private String picUrl4;


}


