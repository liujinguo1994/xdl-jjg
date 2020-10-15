package com.jjg.trade.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 分类运费模板关联表
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
public class EsGoodsFreightDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
	private Long id;

    /**
     * 运费模板id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long modeId;

    /**
     * 商品分类id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long categoryId;

    /**
     * 是否删除(0 不 删除,1删除)
     */
	private Integer isDel;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 修改时间
     */
	private Long updateTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
