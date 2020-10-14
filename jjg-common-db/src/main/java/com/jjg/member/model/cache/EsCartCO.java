package com.jjg.member.model.cache;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;



/**
 * <p>
 * 购物车
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsCartCO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
	private Long memberId;
	/**
	 * 购物车项
	 */
    private List<EsCommercelItemsCO> esCommercelItemsCOS;
}
