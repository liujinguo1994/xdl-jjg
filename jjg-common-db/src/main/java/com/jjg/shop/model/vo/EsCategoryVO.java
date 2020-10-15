package com.jjg.shop.model.vo;

import com.shopx.goods.api.model.domain.EsCategoryDO;
import com.shopx.goods.api.model.domain.ParameterGroupDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * VO
 * </p>
 *
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsCategoryVO implements Serializable {

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

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
	private Integer categoryOrder;

    /**
     * 分类图标
     */
	@ApiModelProperty(value = "分类图标")
	private String image;
	/**
	 * 分类子列表
	 */
	@ApiModelProperty(value = "分类子列表")
	private List<EsCategoryDO> children;
	@ApiModelProperty(value = "参数集合")
	private List<ParameterGroupDO> parameterList;
}
