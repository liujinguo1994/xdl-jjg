package com.xdl.jjg.model.domain;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: EsSettlement
 * @Description: 结算单封装类
 * @Author: bamboo  asp.bamboo@gmail.com
 * @Date: 2019/8/30 15:45
 * @Version: 1.0
 */
@Data
public class EsSettlement {

    /**
     * 数据总数
     */
    private Long total;


    /**
     * 账单详情（结算单信息）
     */
    private List<EsBillDetailDO> billDetailList;

    /**
     * 结算单详情
     */
    List<EsSettlementDetailDO> settlementDetailList;
}
