package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 休眠会员
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsSleepMemberDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员类型 0新增，1活跃，2休眠,3普通
     */
    private Integer memberType;

    /**
     * 未访问该店铺天数
     */
    private Integer visitDays;
    /**
     * 连续多少天无订单
     */
    private Integer days;


}
