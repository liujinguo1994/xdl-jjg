package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 导出会员
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-03-27
 */
@Data
@ApiModel
public class EsExportMemberVO implements Serializable {

    private static final long serialVersionUID = 2561684726141239272L;

    @ApiModelProperty(value = "会员ID")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "会员性别(1,男 0，女)")
    private Integer sex;

    @ApiModelProperty(value = "会员性别(1,男 0，女)")
    private String sexText;

    @ApiModelProperty(value = "身份证号")
    private String identity;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "会员等级")
    private String memberLevel;

    @ApiModelProperty(value = "成长值")
    private Integer grade;

    @ApiModelProperty(value = "可用积分")
    private Long consumPoint;

    @ApiModelProperty(value = "账号余额")
    private Double memberBalance;

    @ApiModelProperty(value = "注册时间")
    private Long createTime;

    @ApiModelProperty(value = "会员状态(0,正常 1，禁用)")
    private Integer state;

    @ApiModelProperty(value = "企业标识符")
    private String companyCode;

    @ApiModelProperty(value = "签约公司")
    private String company;


















}