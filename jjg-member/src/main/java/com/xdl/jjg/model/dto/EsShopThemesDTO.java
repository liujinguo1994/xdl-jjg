package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 店铺模版
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsShopThemesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模版id
     */
	private Long id;

    /**
     * 模版名称
     */
	private String name;

    /**
     * 图片模板路径
     */
	private String imagePath;

    /**
     * 是否为默认(是否为默认 0为默认 反之为1)
     */
	private Integer isDefault;

    /**
     * 模版类型
     */
	private String type;

    /**
     * 模版标识
     */
	private String mark;

	protected Serializable pkVal() {
		return this.id;
	}

}
