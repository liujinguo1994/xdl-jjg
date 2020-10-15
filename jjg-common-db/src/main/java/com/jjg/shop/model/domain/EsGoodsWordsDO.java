package com.jjg.shop.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 自定义分词
 * </p>
 *
 * @author WANGAF 826988665@qq.com
 * @since 2019-07-01 13:54:20
 */
@Data
public class EsGoodsWordsDO  implements  Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分词名称
     */
	private String words;

    /**
     * 商品数量
     */
	private Long goodsNum;

    /**
     * 全拼
     */
	private String quanpin;

    /**
     * 首字母
     */
	private String szm;

}
