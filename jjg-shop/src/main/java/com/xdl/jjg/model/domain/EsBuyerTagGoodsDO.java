package com.jjg.member.model.domain;/**
 * @author wangaf
 * @date 2019/11/2 14:37
 **/

import lombok.Data;

import java.io.Serializable;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/11/2
 @Version V1.0
 **/
@Data
public class EsBuyerTagGoodsDO implements Serializable {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 商品名称
     */

    private String goodsName;
    /**
     * 商品编号
     */

    private String goodsSn;

    /**
     * 商品价格
     */

    private Double money;

    /**
     * 可用库存=SKU之和
     */

    private Integer quantity;
    /**
     * 购买数量
     */

    private Integer buyCount;
    /**
     * 原图路径
     */

    private String original;
}
