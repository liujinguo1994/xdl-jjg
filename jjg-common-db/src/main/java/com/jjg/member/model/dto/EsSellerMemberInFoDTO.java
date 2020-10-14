package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 卖家端会员信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsSellerMemberInFoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 用户名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 最近浏览时间
     */
    private Long scanneTime;
    /**
     * 最近下单时间
     */
    private Long orderTime;
    /**
     * 店铺会员类型
     */
    private String type;

}
