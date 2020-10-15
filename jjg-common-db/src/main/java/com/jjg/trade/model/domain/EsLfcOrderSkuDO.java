package com.jjg.trade.model.domain;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.shopx.trade.api.utils.PropertyUpperStrategy;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;

/**
 * 中国人寿订单
 * @author Snow create in 2018/5/14
 * @version v2.0
 * @since v7.0.0
 */
@ApiIgnore
@JsonNaming(value = PropertyUpperStrategy.UpperCamelCaseStrategy.class)
public class EsLfcOrderSkuDO implements Serializable {

    @ApiModelProperty(value = "SKU ID")
    private String skuId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品数量")
    private Integer saleCount;


    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    @Override
    public String toString() {
        return "LfcOrderSku{" +
                "skuId='" + skuId + '\'' +
                ", productName='" + productName + '\'' +
                ", saleCount=" + saleCount +
                '}';
    }
}
