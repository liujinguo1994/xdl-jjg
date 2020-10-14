package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 查询会员类型
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsQueryMemberTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员类型
     */
    private Integer memberType;
    /**
     * 会员手机号
     */
    private String mobile;
    /**
     * 会员邮箱
     */
    private String email;
    /**
     * 会员名称
     */
    private String name ;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 最近下单开始时间
     */
    private Long beginTime;
    /**
     * 最近下单结束时间
     */
    private Long endTime;
    /**
     * 当前操作者id
     */
    private Long userId;



}
