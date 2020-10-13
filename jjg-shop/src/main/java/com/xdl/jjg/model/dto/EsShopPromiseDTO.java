package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-18 09:39:17
 */
@Data
public class EsShopPromiseDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 卖家id
     */
	private Long shopId;

    /**
     * 承诺内容
     */
	private String content;

    /**
     * 有效状态
     */
	private Integer state;



}
