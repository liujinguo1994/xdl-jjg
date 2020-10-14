package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * @author fk
 * @version v1.0
 * @Description: 商家查询商品使用
 * @date 2018/5/21 14:36
 * @since v7.0.0
 */
@ApiModel
public class ItemSkuSpecValueListVO implements Serializable {

    private static final long serialVersionUID = 3922135264669953741L;
    private SkuSpecVO skuSpec;

    private SkuSpecValueVO skuSpecValue;

    public SkuSpecVO getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(SkuSpecVO skuSpec) {
        this.skuSpec = skuSpec;
    }

    public SkuSpecValueVO getSkuSpecValue() {
        return skuSpecValue;
    }

    public void setSkuSpecValue(SkuSpecValueVO skuSpecValue) {
        this.skuSpecValue = skuSpecValue;
    }
}


