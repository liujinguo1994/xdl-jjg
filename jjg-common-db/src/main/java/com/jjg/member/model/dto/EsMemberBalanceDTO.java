package com.jjg.member.model.dto;

import com.shopx.member.api.model.domain.enums.ConsumeEnumType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsMemberBalanceDTO implements Serializable {

    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 类型
     */
    private ConsumeEnumType type;

    /**
     * 金额
     */
    private Double money;

    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 交易编号
     */
    private String tradeSn;

    /**
     * 当前账户余额
     */
    private Double memberBalance;

}
