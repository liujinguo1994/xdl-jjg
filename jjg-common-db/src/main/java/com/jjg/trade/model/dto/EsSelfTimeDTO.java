package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 自提时间
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsSelfTimeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 开始时间
     */
	private Long startTime;
    /**
     * 结束时间
     */
	private Long endTime;
    /**
     * 人数
     */
	private Integer personNumber;
	/**
	 * 当前自提人数
	 */
	private Integer currentPerson;

	/**
	 * 自提日期ID
	 */
	private Long dateId;


	protected Serializable pkVal() {
		return this.id;
	}

}
