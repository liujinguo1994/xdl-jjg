package com.jjg.member.model.dto;

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
public class EsSeckillRangeDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 限时抢购活动id
     */
	private Long seckillId;
    /**
     * 整点时刻
     */
	private Integer rangeTime;


	protected Serializable pkVal() {
		return this.id;
	}

}
