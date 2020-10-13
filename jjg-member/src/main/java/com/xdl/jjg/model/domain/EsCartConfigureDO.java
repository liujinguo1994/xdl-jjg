package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 购物车配置
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsCartConfigureDO implements Serializable {


    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品数量
     */
	private Integer quantity;

}
