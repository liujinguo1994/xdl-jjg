package com.jjg.member.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员表-后台
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberAdminDO implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    private Long id;

    /**
     * 会员登陆用户名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 会员登陆密码
     */
    private String password;

    /**
     * 会员性别(1：男，2：女)
     */
    private Integer sex;

    /**
     * 会员生日
     */
    private Long birthday;

    /**
     * 所属省份ID
     */
    private Long provinceId;

    /**
     * 所属城市ID
     */
    private Long cityId;

    /**
     * 所属省份名称
     */
    private String province;

    /**
     * 所属城市名称
     */
    private String city;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 座机号码
     */
    private String tel;

    /**
     * 成长值
     */
    private Integer grade;

    /**
     * 可用积分
     */
    private Long consumPoint;

    /**
     * 上次登陆时间
     */
    private Long lastLogin;

    /**
     * 邮件是否已验证
     */
    private Integer isCheked;

    /**
     * 注册IP地址
     */
    private String registerIp;

    /**
     * 会员信息是否完善
     */
    private Integer infoFull;

    /**
     * 会员头像
     */
    private String face;

    /**
     * 身份证号
     */
    private String identity;

    /**
     * 会员状态(0,正常 1，禁用)
     */
    private Integer state;

    /**
     * QQ账号
     */
    private String qqId;

    /**
     * 微信账号
     */
    private String wechatId;

    /**
     * 微博账号
     */
    private String weiboId;

    /**
     * 支付宝账号
     */
    private String alipayId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 余额
     */
    private Double memberBalance;

    /**
     * 企业标识符
     */
    private String companyCode;

    /**
     * 会员创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Long createTime;

    private Long updateTime;

    /**
     * 盐
     */
    private String salt;

    /**
     * 身份证姓名
     */
    private String cardName;
}
