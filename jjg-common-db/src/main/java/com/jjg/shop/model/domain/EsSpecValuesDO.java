package com.jjg.shop.model.domain;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
public class EsSpecValuesDO implements  Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 规格项id
     */
	private Long specId;
    /**
     * 规格值名字
     */
	private String specValue;
    /**
     * 所属卖家
     */
	private Long shopId;
    /**
     * 规格名称
     */
	private String specName;
	protected Serializable pkVal() {
		return this.id;
	}

}
