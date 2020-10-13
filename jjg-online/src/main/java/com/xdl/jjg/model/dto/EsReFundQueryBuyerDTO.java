package com.xdl.jjg.model.dto;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description: 买家端 售后单查询DTO
 * @Author       LiuJG 344009799@qq.com
 * @Date         2019/12/12 10:00
 *
 */
@ApiModel
@Data
public class EsReFundQueryBuyerDTO extends QueryPageForm {

    private static final long serialVersionUID = -7349392509774931388L;
    /**
     * 输入框输入值（订单编号,商品名称）
     */
    private String keyword;

    /**
     * 买家端 售后列表 会员ID
     */
    private Long memberId;

    /**
     * 买家端 查询时间段
     */
    private Integer time;

    /**
     * 订单状态
     */
    private String orderState;

    /**
     * 售后状态
     */
    private String serviceState;

}
