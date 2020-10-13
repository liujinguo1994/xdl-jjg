package com.xdl.jjg.model.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsOrderMetaForm implements Serializable{

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


	protected Serializable pkVal() {
		return this.id;
	}

}
