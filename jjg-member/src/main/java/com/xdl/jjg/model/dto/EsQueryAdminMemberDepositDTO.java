package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 后台查询会员余额明细
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsQueryAdminMemberDepositDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 操作类型(0充值，1消费，2退款)
     */
    private String type;
    /**
     * 交易开始时间
     */
    private Long createTimeStart;
    /**
     * 交易结束时间
     */
    private Long createTimeEnd;


}
