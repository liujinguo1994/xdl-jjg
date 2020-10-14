package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModelProperty;
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
public class EsShopThemesVO implements Serializable {


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

    @ApiModelProperty(name="current_use",value="当前是否使用 1 是 0 否",required=true)
    private Integer currentUse;

}
