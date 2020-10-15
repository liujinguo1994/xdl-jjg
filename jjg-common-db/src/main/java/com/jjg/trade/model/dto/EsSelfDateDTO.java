package com.jjg.trade.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自提日期
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsSelfDateDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 自提日期
     */
	private Long selfDate;
	/**
	 * 有效状态
	 */
	private Integer state;
	/**
	 * 自提时间段
	 */
	private List<EsSelfTimeDTO> selfTimeDTOList;


	protected Serializable pkVal() {
		return this.id;
	}

}
