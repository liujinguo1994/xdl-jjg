package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *移动端-个人资料设置
 */
@Data
@ApiModel
public class EsWapSetMemberInfoForm implements Serializable {

    private static final long serialVersionUID = -8157071476082647088L;
    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private Long id;

    /**
     * 会员头像
     */
    @ApiModelProperty(value = "会员头像")
    private String face;

    /**
     * 会员登陆用户名
     */
    @ApiModelProperty(value = "会员登陆用户名")
    private String name;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

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
}
