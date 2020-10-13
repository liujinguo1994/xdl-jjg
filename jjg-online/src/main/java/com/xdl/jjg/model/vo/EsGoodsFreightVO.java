package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 分类运费模板关联表VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGoodsFreightVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
	@ApiModelProperty(value = "自增主键")
	private Long id;

    /**
     * 运费模板id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "运费模板id")
	private Long modeId;

    /**
     * 商品分类id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商品分类id")
	private Long categoryId;

    /**
     * 是否删除(0 不 删除,1删除)
     */
	@ApiModelProperty(value = "是否删除(0 不 删除,1删除)")
	private Integer isDel;

    /**
     * 创建时间
     */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

    /**
     * 修改时间
     */
	@ApiModelProperty(value = "修改时间")
	private Long updateTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
