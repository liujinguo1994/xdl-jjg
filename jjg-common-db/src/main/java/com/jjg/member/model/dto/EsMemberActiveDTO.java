package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 活跃会员参数
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberActiveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增会员
     */
    private EsAddMemberDTO esAddMemberDTO;

    /**
     * 活跃会员
     */
    private EsActiveMemberDTO esActiveMemberDTO;

    /**
     * 休眠会员
     */
    private EsSleepMemberDTO esSleepMemberDTO;

    /**
     * 操作者id
     */
    private Long userId;

}
