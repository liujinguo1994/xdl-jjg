package com.jjg.shop.model.domain;
import lombok.Data;

import java.io.Serializable;


/**
 * 人寿商品
 * @author yuanj
 * @version v2.0
 * @since v7.0.0
 * 2020-03-25 16:32:45
 */
@Data
public class EsLfcGoodsDO implements Serializable {
			
    private static final long serialVersionUID = 9122931201151887L;

    /**
     * 主键ID
     */
    private Long id;
    /**商品id*/
    private Long goodsId;

    /**审核状态 1审核通过 2 待审核*/
    private Integer isAuth;

}