package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 会员表VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-12-11 09:28:27
 */
@Data
@Api
@Accessors(chain = true)
public class EsMemberVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
	@ApiModelProperty(value = "会员ID")
	private Long id;

    /**
     * 会员登陆用户名
     */
	@ApiModelProperty(value = "会员登陆用户名")
	private String name;

    /**
     * 邮箱
     */
	@ApiModelProperty(value = "邮箱")
	private String email;

    /**
     * 会员登陆密码
     */
	@ApiModelProperty(value = "会员登陆密码")
	private String password;

    /**
     * 会员性别(1：男，2：女)
     */
	@ApiModelProperty(value = "会员性别(1：男，2：女)")
	private Integer sex;

    /**
     * 会员生日
     */
	@ApiModelProperty(value = "会员生日")
	private Long birthday;

    /**
     * 所属省份ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "所属省份ID")
	private Long provinceId;

    /**
     * 所属城市ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "所属城市ID")
	private Long cityId;

    /**
     * 所属省份名称
     */
	@ApiModelProperty(value = "所属省份名称")
	private String province;

    /**
     * 所属城市名称
     */
	@ApiModelProperty(value = "所属城市名称")
	private String city;

    /**
     * 手机号码
     */
	@ApiModelProperty(value = "手机号码")
	private String mobile;

	/**
	 * 新手机号
	 */
	@ApiModelProperty(value = "新手机号")
	private String newMobile;

    /**
     * 座机号码
     */
	@ApiModelProperty(value = "座机号码")
	private String tel;

    /**
     * 成长值
     */
	@ApiModelProperty(value = "成长值")
	private Integer grade;

    /**
     * 可用积分
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "可用积分")
	private Long consumPoint;

    /**
     * 上次登陆时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "上次登陆时间")
	private Long lastLogin;

    /**
     * 邮件是否已验证
     */
	@ApiModelProperty(value = "邮件是否已验证")
	private Integer isCheked;

    /**
     * 注册IP地址
     */
	@ApiModelProperty(value = "注册IP地址")
	private String registerIp;

    /**
     * 会员信息是否完善
     */
	@ApiModelProperty(value = "会员信息是否完善")
	private Integer infoFull;

    /**
     * 会员头像
     */
	@ApiModelProperty(value = "会员头像")
	private String face;

    /**
     * 身份证号
     */
	@ApiModelProperty(value = "身份证号")
	private String identity;

    /**
     * 会员状态(0,正常 1，禁用)
     */
	@ApiModelProperty(value = "会员状态(0,正常 1，禁用)")
	private Integer state;

    /**
     * QQ账号
     */
	@ApiModelProperty(value = "QQ账号")
	private String qqId;

    /**
     * 微信账号
     */
	@ApiModelProperty(value = "微信账号")
	private String wechatId;

    /**
     * 微博账号
     */
	@ApiModelProperty(value = "微博账号")
	private String weiboId;

    /**
     * 支付宝账号
     */
	@ApiModelProperty(value = "支付宝账号")
	private String alipayId;

    /**
     * 昵称
     */
	@ApiModelProperty(value = "昵称")
	private String nickname;

    /**
     * 余额
     */
	@ApiModelProperty(value = "余额")
	private Double memberBalance;

    /**
     * 企业标识符
     */
	@ApiModelProperty(value = "企业标识符")
	private String companyCode;

    /**
     * 会员创建时间
     */
	@ApiModelProperty(value = "会员创建时间")
	private Long createTime;

    /**
     * 盐
     */
	@ApiModelProperty(value = "盐")
	private String salt;

    /**
     * 身份证姓名
     */
	@ApiModelProperty(value = "身份证姓名")
	private String cardName;
	/**
	 * 用户唯一标识
	 */
	@ApiModelProperty(value = "用户唯一标识")
	private String openid;
	/**
	 * 登录态标识
	 */
	@ApiModelProperty(value = "登录态标识")
	private String skey;

	/**
	 * 黑卡标识
	 */
	@ApiModelProperty(value = "黑卡标识")
	private Integer blackCard;

	/**
	 * 黑卡折扣力度
	 */
	@ApiModelProperty(value = "黑卡折扣力度")
	private Double blackDiscount;

	protected Serializable pkVal() {
		return this.id;
	}

}
