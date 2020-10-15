package com.jjg.shop.model.domain;
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
public class EsShopPromiseDO implements  Serializable {


    /**
     * 主键
     */
	private Long id;

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

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 更新时间
     */
	private Long updateTime;

}
