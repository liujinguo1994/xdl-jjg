package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 后台会员列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
public class EsAdminMemberDO implements Serializable {

    /**
     * 会员id
     */
    private Long id;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 会员等级
     */
    private String memberLevel;
    /**
     * 签约公司
     */
    private String company;
    /**
     * 账号余额
     */
    private Double money;
    /**
     * 注册时间
     */
    private Long registerTime;
    /**
     *登陆时间
     */
    private Long loginTime;
    /**
     * 会员状态
     */
    private String state;
    /**
     * 成长值
     */
    private Integer grade;
    /**
     * 名称
     */
    private String name;

    private Long lastLogin;
}
