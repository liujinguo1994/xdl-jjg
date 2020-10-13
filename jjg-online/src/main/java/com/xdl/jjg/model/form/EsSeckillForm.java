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
public class EsSeckillForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;
    /**
     * 活动名称
     */
	private String seckillName;
    /**
     * 活动日期
     */
	private Long createTime;
    /**
     * 报名截至时间
     */
	private Long updateTime;
    /**
     * 申请规则
     */
	private String seckillRule;
    /**
     * 商家id集合以逗号分隔
     */
	private String shopIds;
    /**
     * 状态（1编辑中2已发布3已结束）
     */
	private Integer state;


	protected Serializable pkVal() {
		return this.id;
	}

}
