package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class EsGoodsGalleryVO implements Serializable {
    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * skuID
     */
	@ApiModelProperty(value = "skuID")
	private Long skuId;

    /**
     * 缩略图路径
     */
	@ApiModelProperty(value = "缩略图路径")
	private String image;
    /**
     * 排序
     */
	@ApiModelProperty(value = "排序")
	private Integer sort;
	@ApiModelProperty(value = "相册组编号")
	private String albumNo;
}
