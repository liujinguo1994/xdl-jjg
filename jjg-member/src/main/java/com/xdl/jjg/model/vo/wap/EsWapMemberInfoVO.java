package com.xdl.jjg.model.vo.wap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 移动端-会员信息
 * </p>
 *
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsWapMemberInfoVO implements Serializable {

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
	 * 会员头像
	 */
	@ApiModelProperty(value = "会员头像")
	private String face;

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
     * 手机号码
     */
	@ApiModelProperty(value = "手机号码")
	private String mobile;

    /**
     * 可用积分
     */
	@ApiModelProperty(value = "可用积分")
	private Long consumPoint;

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
	 * 成长值
	 */
	@ApiModelProperty(value = "成长值")
	private Integer grade;

	/**
	 * 会员等级
	 */
	@ApiModelProperty(value = "会员等级")
	private String gradeLevel;

	/**
	 * 是否存在密码
	 */
	@ApiModelProperty(value = "是否存在密码")
	private String passWordIsExist;
}
