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
public class EsSpecValuesDTO implements Serializable  {

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
     * 规格名称
     */
	private String specName;
	protected Serializable pkVal() {
		return this.id;
	}

}
