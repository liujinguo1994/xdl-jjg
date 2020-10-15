package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺模版
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsShopThemesDO implements Serializable {


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
     * 是否为默认
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

    private Integer currentUse;

}
