package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店员
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-02
 */
@Data
@ToString
public class GoodsNoticeDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品信息
     */
	private String goodsInfo;

    /**
     * 商品类型
     */
	private String categoryName;

    /**
     * 价格
     */
	private Double price;



}
