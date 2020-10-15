package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

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
public class EsSeckillRangeDO extends Model<EsSeckillRangeDO> {

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


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
