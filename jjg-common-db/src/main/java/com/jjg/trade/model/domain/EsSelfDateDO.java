package com.jjg.member.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

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
public class EsSelfDateDO extends Model<EsSelfDateDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 自提日期
     */
	private Long selfDate;
//    /**
//     * 自提点ID
//     */
//	private Long deliveryId;
	/**
	 * 限制总人数
	 */
	@TableField(exist = false)
	private Integer personTotal;

	private List<EsSelfTimeDO> selfTimeDOList;
	/**
	 * 有效状态
	 */
	private Integer state;
	/**
	 * 是否选中
	 */
	private Boolean selected;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
