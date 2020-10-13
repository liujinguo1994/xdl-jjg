package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺导航管理
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsNavigationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 导航id
     */
	private Long id;

    /**
     * 名称
     */
	private String name;

    /**
     * 是否显示
     */
	private Integer isDel;

    /**
     * 排序
     */
	private Integer sort;

    /**
     * 导航内容
     */
	private String contents;

    /**
     * URL
     */
	private String navUrl;

    /**
     * 新窗口打开
     */
	private String target;

    /**
     * 店铺id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

	protected Serializable pkVal() {
		return this.id;
	}

}
