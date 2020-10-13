package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员积分明细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberPointHistoryDO implements Serializable {


    /**
     * 主键ID
     */
	private Long id;
    /**
     * 会员ID
     */
	private Long memberId;
    /**
     * 积分值
     */
	private Integer gradePoint;
    /**
     * 操作时间
     */
	private Long createTime;
    /**
     * 操作理由0：其他送 1：购物送 2：评论送
     */
	private Integer reason;
    /**
     * 积分类型 1为增加，0为消费
     */
	private Integer gradePointType;
    /**
     * 操作者
     */
	private String operator;

    /**
     * 当前积分
     */
	private Integer currentPoint;

    /**
     * 订单编号
     */
    private String orderSn;
}
