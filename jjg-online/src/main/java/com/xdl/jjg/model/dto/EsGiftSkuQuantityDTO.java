package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/8/8 13:54
 */
@Data
public class EsGiftSkuQuantityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 可用库存
     */
    private Integer enableStore;

    /**
     * 档案SKUid
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long skuiId;

    /**
     * 店铺id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long shopId;

    private Long goodsId;

    protected Serializable pkVal() {
        return this.id;
    }
}
