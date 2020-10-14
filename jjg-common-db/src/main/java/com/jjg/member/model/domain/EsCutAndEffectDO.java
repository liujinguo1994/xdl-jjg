package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 降价和失效商品数量
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsCutAndEffectDO implements Serializable {

    /**
     * 降价商品数量
     */
    private Integer cutPricNum;
    /**
     * 失效商品数量
     */
    private Integer effectNum;


}
