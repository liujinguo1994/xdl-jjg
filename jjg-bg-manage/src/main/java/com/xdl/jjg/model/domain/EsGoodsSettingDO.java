package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 商品相关的系统设置
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
public class EsGoodsSettingDO implements Serializable{


    private static final long serialVersionUID = -4237676288718760409L;

    //上架是否需要审核
    private Integer marcketAuth;

    //修改商品是否需要审核
    private Integer updateAuth;

    //缩略图宽度
    private Integer thumbnailWidth;

    //缩略图高度
    private Integer thumbnailHeight;

    //小图宽度
    private Integer smallWidth;

   //小图高度
    private Integer smallHeight;

    //大图宽度
    private Integer bigWidth;

   //大图高度
    private Integer bigHeight;

}
