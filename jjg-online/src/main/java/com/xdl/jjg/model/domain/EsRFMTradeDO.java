package com.xdl.jjg.model.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员订单RFM表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsRFMTradeDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 最近一次消费
     */
    private Integer recency;
    /**
     * 消费频率
     */
    private Integer frequency;
    /**
     * 消费金额
     */
    private Double monetary;
    /**
     * 超过多少天标识 1标识超过多少天
     */
    private Integer sign;

}
