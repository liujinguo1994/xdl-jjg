package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员积分明细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberPointHistoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 操作理由
     */
    private String reason;

    /**
     * 积分类型 1为增加，0为消费
     */
    private Integer gradePointType;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 订单编号
     */
    private String orderSn;

}
