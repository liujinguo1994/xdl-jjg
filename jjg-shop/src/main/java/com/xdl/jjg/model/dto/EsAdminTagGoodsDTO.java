package com.jjg.member.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-07-27 14:57:57
 */
@Data
@Accessors(chain = true)
public class EsAdminTagGoodsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */

	private Long tagId;

    /**
     * 商品id
     */

	private Long goodsId;


}
