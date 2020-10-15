package com.jjg.shop.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.member.model.domain.EsGoodsDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class EsBuyerGoodsDO implements  Serializable {

	private Long categoryId;

	private String categoryName;

	private List<EsGoodsDO> goodsList;


}
