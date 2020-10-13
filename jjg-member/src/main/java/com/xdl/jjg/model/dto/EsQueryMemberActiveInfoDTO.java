package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员活跃度配置表
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-02 19:50:03
 */
@Data
@ToString
public class EsQueryMemberActiveInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增天数限制
     */
    private Integer addDays;
    /**
     * 新增订单限制
     */
    private Integer addOrders;
    /**
     * 活跃天数限制
     */
    private Integer activeDays;
    /**
     * 活跃订单限制
     */
    private Integer activeOrders;
    /**
     * 未登陆天数
     */
    private Integer outLandDays;
    /**
     * 无订单数量
     */
    private Integer noOrders;
    /**
     * 店鋪id
     */
    private Long shopId;
    /**
     * 活跃开始日期
     */
    private String activeTime;
    /**
     * 新增开始日期
     */
    private String addTime;
    /**
     * 未登陆开始日期
     */
    private String outLandTime;
    /**
     * 未下订单开始日期
     */
    private String noOrderTime;
    /**
     * 会员类型
     */
    private String  memberType;
    /**
     * 搜索内容
     */
    private String coent;
    /**
     * 下单开始时间
     */
    private Long beginTime;
    /**
     * 下单结束时间
     */
    private Long endTime;


}
