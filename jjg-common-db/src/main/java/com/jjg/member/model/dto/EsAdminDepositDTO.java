package com.jjg.member.model.dto;

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
public class EsAdminDepositDTO implements Serializable {


    /**
     * 会员ID
     */
    private Long id;
    /**
     * 会员登陆用户名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 密码
     */
    private String password;
    /**
     * 企业标识符
     */
    private String companyCode;
    /**
     * 余额
     */
    private Double memberBalance;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 充值金额
     */
    private Double money;


}
