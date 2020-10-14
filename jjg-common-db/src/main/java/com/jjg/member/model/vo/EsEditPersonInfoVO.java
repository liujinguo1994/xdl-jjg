package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 编辑个人信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsEditPersonInfoVO implements Serializable {


    /**
     * 会员头像
     */
    @ApiModelProperty(required = false, value = "会员头像")
    private String face;
    /**
     * 登录名
     */
    @ApiModelProperty(required = false, value = "登录名")
    private String name;
    /**
     * 会员性别(1：男，2：女)
     */
    @ApiModelProperty(required = false, value = "会员性别(1：男，2：女)", example = "1")
    private Integer sex;
    /**
     * 会员生日
     */
    @ApiModelProperty(required = false, value = "会员生日", example = "1")
    private Long birthday;
    /**
     * 邮箱
     */
    @ApiModelProperty(required = false, value = "邮箱", example = "1")
    private String email;
    /**
     * 真实姓名
     */
    @ApiModelProperty(required = false, value = "真实姓名")
    private String cardName;
    /**
     * 会员等级
     */
    @ApiModelProperty(required = false, value = "会员等级")
    private String gradeLevel;

    /**
     * 会员等级
     */
    @ApiModelProperty(required = false, value = "会员等级")
    private Double memberBalance;


    /**
     * 会员ID
     */
    @ApiModelProperty(required = false, value = "会员ID")
    private Long id;

    /**
     * 会员登陆密码
     */
    @ApiModelProperty(required = false, value = "会员登陆密码")
    private String password;

    /**
     * 所属省份ID
     */
    @ApiModelProperty(required = false, value = "所属省份ID")
    private Long provinceId;
    /**
     * 所属城市ID
     */
    @ApiModelProperty(required = false, value = "所属城市ID")
    private Long cityId;
    /**
     * 所属省份名称
     */
    @ApiModelProperty(required = false, value = "所属省份名称")
    private String province;
    /**
     * 所属城市名称
     */
    @ApiModelProperty(required = false, value = "所属城市名称")
    private String city;
    /**
     * 手机号码
     */
    @ApiModelProperty(required = false, value = "手机号码")
    private String mobile;
    /**
     * 新手机号
     */
    @ApiModelProperty(required = false, value = "新手机号")
    private String newMobile;
    /**
     * 座机号码
     */
    @ApiModelProperty(required = false, value = "座机号码")
    private String tel;
    /**
     * 成长值
     */
    @ApiModelProperty(required = false, value = "成长值")
    private Integer grade;
    /**
     * 可用积分
     */
    @ApiModelProperty(required = false, value = "可用积分")
    private Long consumPoint;
    /**
     * 上次登陆时间
     */
    @ApiModelProperty(required = false, value = "上次登陆时间")
    private Long lastLogin;
    /**
     * 邮件是否已验证
     */
    @ApiModelProperty(required = false, value = "邮件是否已验证")
    private Integer isCheked;
    /**
     * 注册IP地址
     */
    @ApiModelProperty(required = false, value = "注册IP地址")
    private String registerIp;
    /**
     * 会员信息是否完善
     */
    @ApiModelProperty(required = false, value = "会员信息是否完善")
    private Integer infoFull;

    /**
     * 身份证号
     */
    @ApiModelProperty(required = false, value = "身份证号")
    private String identity;
    /**
     * 会员状态(0,正常 1，禁用)
     */
    @ApiModelProperty(required = false, value = "会员状态(0,正常 1，禁用)")
    private Integer state;
    /**
     * QQ账号
     */
    @ApiModelProperty(required = false, value = "QQ账号")
    private String qqId;
    /**
     * 微信账号
     */
    @ApiModelProperty(required = false, value = "微信账号")
    private String wechatId;
    /**
     * 微博账号
     */
    @ApiModelProperty(required = false, value = "微博账号")
    private String weiboId;
    /**
     * 支付宝账号
     */
    @ApiModelProperty(required = false, value = "支付宝账号")
    private String alipayId;
    /**
     * 昵称
     */
    @ApiModelProperty(required = false, value = "昵称")
    private String nickname;

    /**
     * 企业标识
     */
    @ApiModelProperty(required = false, value = "企业标识")
    private String companyCode;

    /**
     * token
     */
    @ApiModelProperty(required = false, value = "token")
    private String token;


}
