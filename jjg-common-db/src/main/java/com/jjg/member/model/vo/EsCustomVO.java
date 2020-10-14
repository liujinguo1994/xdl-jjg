package com.jjg.member.model.vo;

import com.shopx.member.api.model.domain.EsCustomDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺自定义分类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-24
 */
@Data
@Api
public class EsCustomVO implements Serializable {

    private static final long serialVersionUID = 8155378858596627368L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
	private Long shopId;
    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
	private String name;
    /**
     * 分类父id
     */
    @ApiModelProperty(value = "分类父id")
	private Long parentId;
    /**
     * 分类父子路径
     */
    @ApiModelProperty(value = "分类父子路径")
	private String categoryPath;
    /**
     * 该分类下商品数量
     */
    @ApiModelProperty(value = "该分类下商品数量")
	private Integer goodsCount;
    /**
     * 分类排序
     */
    @ApiModelProperty(value = "分类排序")
	private Integer sort;
    /**
     * 分类图标
     */
    @ApiModelProperty(value = "分类图标")
	private String image;
    /**
     * 分类子列表
     */
    @ApiModelProperty(value = "分类子列表")
    private List<EsCustomDO> children;

}
