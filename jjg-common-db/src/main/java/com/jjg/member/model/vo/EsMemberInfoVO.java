package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员表VO
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-08-08 16:20:30
 */
@Data
@ApiModel
public class EsMemberInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @ApiModelProperty( required = false,value = "会员ID",example = "1")
    private Long id;

    /**
     * 会员登陆用户名
     */
    @ApiModelProperty( required = false,value = "会员登陆用户名")
    private String name;

    /**
     * 邮箱
     */
    @ApiModelProperty( required = false,value = "邮箱")
    private String email;

    /**
     * 会员登陆密码
     */
    @ApiModelProperty( required = false,value = "会员登陆密码")
    private String password;

    /**
     * 会员性别
     */
    @ApiModelProperty( required = false,value = "会员性别",example = "1")
    private Integer sex;

    /**
     * 会员生日
     */
    @ApiModelProperty(required = false,value = "会员生日",example = "12345")
    private Long birthday;

    /**
     * 所属省份ID
     */
    @ApiModelProperty(required = false,value = "所属省份ID",example = "12345")
    private Long provinceId;

    /**
     * 所属城市ID
     */
    @ApiModelProperty(required = false,value = "所属城市ID",example = "12")
    private Long cityId;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(required = false,value = "所属省份名称",example = "安徽")
    private String province;

    /**
     * 所属城市名称
     */
    @ApiModelProperty(required = false,value = "所属城市名称")
    private String city;

    /**
     * 手机号码
     */
    @ApiModelProperty(required = false,value = "手机号码")
    private String mobile;

    /**
     * 座机号码
     */
    @ApiModelProperty(required = false,value = "座机号码")
    private String tel;

    /**
     * 成长值
     */
    @ApiModelProperty(required = false,value = "成长值")
    private Integer grade;

    /**
     * 可用积分
     */
    @ApiModelProperty(required = false,value = "可用积分",example = "123")
    private Long consumPoint;

    /**
     * 上次登陆时间
     */
    @ApiModelProperty(required = false,value = "上次登陆时间",example = "1234567")
    private Long lastLogin;

    /**
     * 邮件是否已验证
     */
    @ApiModelProperty(required = false,value = "邮件是否已验证",example = "1")
    private Integer isCheked;

    /**
     * 注册IP地址
     */
    @ApiModelProperty(required = false,value = "注册IP地址")
    private String registerIp;

    /**
     * 会员信息是否完善
     */
    @ApiModelProperty(required = false,value = "会员信息是否完善",example = "1")
    private Integer infoFull;

    /**
     * 会员头像
     */
    @ApiModelProperty(required = false,value = "会员头像")
    private String face;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String identity;

    /**
     * 会员状态(0,正常 1，禁用)
     */
    @ApiModelProperty(required = false,value = "会员状态(0,正常 1，禁用)",example = "1")
    private Integer state;

    /**
     * QQ账号
     */
    @ApiModelProperty(required = false,value = "QQ账号")
    private String qqId;

    /**
     * 微信账号
     */
    @ApiModelProperty(required = false,value = "微信账号")
    private String wechatId;

    /**
     * 微博账号
     */
    @ApiModelProperty(required = false,value = "微博账号")
    private String weiboId;

    /**
     * 支付宝账号
     */
    @ApiModelProperty(required = false,value = "支付宝账号")
    private String alipayId;

    /**
     * 昵称
     */
    @ApiModelProperty(required = false,value = "昵称")
    private String nickname;

    /**
     * 余额
     */
    @ApiModelProperty(required = false,value = "余额")
    private Double memberBalance;

    /**
     * 企业标识符
     */
    @ApiModelProperty(required = false,value = "企业标识符")
    private String companyCode;

    /**
     * 会员创建时间
     */
    @ApiModelProperty(required = false,value = "会员创建时间",example = "1234")
    private Long createTime;

    /**
     * 盐
     */
    @ApiModelProperty(required = false,value = "盐")
    private String salt;
    /**
     * token
     */
    @ApiModelProperty(required = false,value = "token")
    private String token;

}
