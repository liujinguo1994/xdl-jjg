package com.jjg.shop.model.dto;

import lombok.Data;
import lombok.ToString;

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
@ToString
public class EsSpecificationDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 规格项名称
     */
	private String specName;
    /**
     * 是否被删除0未 删除   1  删除
     */
	private Integer isDel;
    /**
     * 规格描述
     */
	private String specMemo;
    /**
     * 所属卖家 0属于平台
     */
	private Long shopId;

}
