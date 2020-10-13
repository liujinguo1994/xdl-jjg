package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *  订单拓展信息表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsOrderMetaDO extends Model<EsOrderMetaDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 订单编号
     */
	private String orderSn;
    /**
     * 扩展-键
     */
	private String metaKey;
    /**
     * 扩展-值
     */
	private String metaValue;
    /**
     * 售后状态
     */
	private String state;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
