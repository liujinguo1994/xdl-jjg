package com.jjg.member.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsParamsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品id
     */
	private Long goodsId;
    /**
     * 参数id
     */
	private Long paramId;
    /**
     * 参数名字
     */
	private String paramName;
    /**
     * 参数值
     */
	private String paramValue;

}
