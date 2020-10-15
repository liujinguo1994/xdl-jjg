package com.jjg.trade.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取时间点内的秒杀商品信息使用DTO
 * <br/>
 * @author Kangll
 * @since 2020/6/11 9:33
 */
@Data
public class EsSeckillTimelineGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 日期
     */
    private String day;
    /**
     * 时间点
     */
    private Integer timeline;
}
