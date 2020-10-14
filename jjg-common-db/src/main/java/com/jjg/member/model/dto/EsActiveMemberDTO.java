package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 活跃会员
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsActiveMemberDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 会员类型 0新增，1活跃，2休眠,3普通
     */
    private Integer memberType;
    /**
     * 会员天数
     */
    private Integer days;
    /**
     * 会员订单数
     */
    private Integer orders;
    /**
     *未登陆天数
     */
    private Integer visitDays;


}
