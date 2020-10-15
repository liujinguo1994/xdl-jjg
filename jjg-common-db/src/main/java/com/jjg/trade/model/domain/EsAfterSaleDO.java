package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class EsAfterSaleDO extends Model<EsAfterSaleDO> {

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
	private Long categoryId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
