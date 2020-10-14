package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺幻灯片
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsShopSildeDO implements Serializable {


    /**
     * 幻灯片Id
     */
	private Long id;
    /**
     * 店铺Id
     */
	private Long shopId;
    /**
     * 幻灯片URL
     */
	private String sildeUrl;
    /**
     * 图片URL
     */
	private String img;



}
