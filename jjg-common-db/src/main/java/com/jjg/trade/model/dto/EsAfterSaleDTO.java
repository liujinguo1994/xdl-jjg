package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 售后维护配置
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
public class EsAfterSaleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
	private Long id;

    /**
     * 退货时间
     */
	private Integer returnGoodsTime;

    /**
     * 换货时间
     */
	private Integer changeGoodsTime;

    /**
     * 商品分类ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long categoryId;

	protected Serializable pkVal() {
		return this.id;
	}

}
