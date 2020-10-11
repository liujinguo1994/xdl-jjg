package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsRegionsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 父地区id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;

    /**
     * 路径
     */
	private String regionPath;

    /**
     * 级别
     */
	private Integer regionGrade;

    /**
     * 名称
     */
	private String localName;

    /**
     * 邮编
     */
	private String zipcode;

}
