package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

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
public class EsSelfTimeDO extends Model<EsSelfTimeDO> {

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


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
